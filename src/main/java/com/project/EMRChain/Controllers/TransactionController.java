package com.project.EMRChain.Controllers;
import com.project.EMRChain.Core.Node;
import com.project.EMRChain.Events.GetUserConsentEvent;
import com.project.EMRChain.Exceptions.BadRequestException;
import com.project.EMRChain.Exceptions.ResourceNotFoundException;
import com.project.EMRChain.Payload.Auth.ApiResponse;
import com.project.EMRChain.Payload.Core.BlockAddition;
import com.project.EMRChain.Payload.Core.UserConsentRequest;
import com.project.EMRChain.Services.ChainRootService;
import com.project.EMRChain.Services.ClustersContainer;
import com.project.EMRChain.Utilities.ChainUtil;
import com.project.EMRChain.Utilities.SimpleStringUtil;
import com.project.EMRChain.Utilities.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;

@RestController
@RequestMapping("/transaction")
public class TransactionController
{
    private ApplicationEventPublisher eventPublisher;
    private UuidUtil uuidUtil;
    private SimpleStringUtil simpleStringUtil;
    private ClustersContainer clustersContainer;
    private ChainRootService chainRootService;
    private ChainUtil chainUtil;

    @Autowired
    public TransactionController(ChainRootService chainRootService, ClustersContainer clustersContainer, ApplicationEventPublisher eventPublisher, SimpleStringUtil simpleStringUtil, UuidUtil uuidUtil, ChainUtil chainUtil) {
        this.chainRootService = chainRootService;
        this.clustersContainer = clustersContainer;
        this.eventPublisher = eventPublisher;
        this.simpleStringUtil = simpleStringUtil;
        this.uuidUtil = uuidUtil;
        this.chainUtil = chainUtil;
    }

    @PostMapping("/getConsent")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getUserConsent(@RequestBody BlockAddition blockAddition)
    {
        String providerUUID = blockAddition.getProviderUUID();
        String userID = blockAddition.getEhrUserID();

        // Check if Provider UUID and User ID are valid
        if (!uuidUtil.isValidUUID(providerUUID) || !simpleStringUtil.isValidNumber(userID))
        {
            return new ResponseEntity<>(
                new ApiResponse(false, "Invalid Provider UUID or User ID"),
                HttpStatus.BAD_REQUEST
            );
        }

        // Check if provider exists in providers cluster
        if (!clustersContainer.getChainProviders().existsInCluster(providerUUID)) {
            return new ResponseEntity<>(
                new ApiResponse(false, "You are not in the providers list"),
                HttpStatus.BAD_REQUEST
            );
        }

        // Get provider network uuid
        String providerNetworkUUID = clustersContainer.getChainProviders().getNode(providerUUID).getNetworkUUID();

        // Check if provider's network uuid is valid
        if (providerNetworkUUID == null || providerNetworkUUID.isEmpty() || !uuidUtil.isValidUUID(providerUUID)) {
            return new ResponseEntity<>(
                new ApiResponse(false, "Invalid provider network or doesn't exist"),
                HttpStatus.BAD_REQUEST
            );
        }

        // Chain root from addition request payload
        String chainRoot = blockAddition.getChainRootWithoutBlock();


        /*
        *   Check if provider's chain is valid by Comparing sent chainRoot
        *   with the saved chainRoot (latest valid chain root)
        */

        boolean isValidNetworkChainRoot;

        // Check if sent root is valid
        try
        {
            isValidNetworkChainRoot = chainRootService.checkNetworkChainRoot(providerNetworkUUID, chainRoot);
        }
        catch (BadRequestException Ex)
        {
            return new ResponseEntity<>(
                new ApiResponse(false, Ex.getMessage()),
                HttpStatus.BAD_REQUEST
            );
        }

        // If not valid
        if (!isValidNetworkChainRoot)
        {
            // Fetch the chain from another provider and send it to this node with invalid chain
            try
            {
                chainUtil.fetchChainForNode(providerUUID);
            }
            catch (BadRequestException Ex)
            {
                return new ResponseEntity<>(
                    new ApiResponse(false, "Invalid Chain Root or Bad Chain. Chain fetching failed, caused by: " + Ex.getMessage()),
                    HttpStatus.BAD_REQUEST
                );
            }

            return new ResponseEntity<>(
                new ApiResponse(false, "Invalid Chain Root or Bad Chain, chain fetching request was sent."),
                HttpStatus.BAD_REQUEST
            );
        }

        /*
        Todo-----------------------------------------------------------------|
              Send a notification to the user by adding it on DB for the user
        Todo-----------------------------------------------------------------|
        */

        // Send a user consent request for adding the block to chain
        try
        {
            GetUserConsentEvent getUserConsent = new GetUserConsentEvent(
                    this,
                    blockAddition.getBlock(),
                    providerUUID,
                    userID
            );

            eventPublisher.publishEvent(getUserConsent);
        }
        catch (Exception Ex) {
            return new ResponseEntity<>(
                new ApiResponse(false, Ex.getMessage()),
                HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
            new ApiResponse(true, "User permission request was successfully sent"),
            HttpStatus.ACCEPTED
        );
    }

    @PostMapping("/giveConsent")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity giveUserConsent(@RequestBody BlockAddition blockAddition)
    {
        return null;
    }

    @EventListener
    protected void getUserConsent(GetUserConsentEvent event) throws IOException
    {
        String userID = event.getUserID();

        // If user doesn't exist in users cluster
        if (!clustersContainer.getAppUsers().existsInCluster(userID)) {
            throw new ResourceNotFoundException("The User ID", "userID", userID);
        }

        UserConsentRequest userConsentRequest = new UserConsentRequest(
                event.getBlock(),
                event.getProviderUUID(),
                userID
        );

        // Get user emitter from users cluster
        SseEmitter userEmitter = clustersContainer.getAppUsers().getNodeEmitter(userID);

        // Send the consent request with block data to user
        try {
            userEmitter.send(userConsentRequest, MediaType.APPLICATION_JSON);
        }
        catch (IOException Ex) {
            clustersContainer.getAppUsers().removeNode(userID);
        }

    }
}
