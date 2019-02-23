package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Core.Block;
import com.project.EhrRoute.Core.BlockBroadcaster;
import com.project.EhrRoute.Core.Transaction;
import com.project.EhrRoute.Core.Utilities.KeyUtil;
import com.project.EhrRoute.Core.Utilities.RsaUtil;
import com.project.EhrRoute.Entities.App.Notification;
import com.project.EhrRoute.Entities.EHR.EhrDetails;
import com.project.EhrRoute.Entities.EHR.MedicalRecord;
import com.project.EhrRoute.Entities.EHR.PatientInfo;
import com.project.EhrRoute.Events.GetUserConsentEvent;
import com.project.EhrRoute.Entities.Core.ConsentRequestBlock;
import com.project.EhrRoute.Exceptions.BadRequestException;
import com.project.EhrRoute.Exceptions.GeneralAppException;
import com.project.EhrRoute.Exceptions.ResourceEmptyException;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Payload.Core.BlockAddition;
import com.project.EhrRoute.Payload.Core.UserConsentRequest;
import com.project.EhrRoute.Payload.Core.UserConsentResponse;
import com.project.EhrRoute.Services.*;
import com.project.EhrRoute.Utilities.ModelMapper;
import com.project.EhrRoute.Utilities.UuidUtil;
import com.project.EhrRoute.Utilities.ChainUtil;
import com.project.EhrRoute.Utilities.SimpleStringUtil;
import com.project.EhrRoute.Payload.Core.SerializableBlock;
import org.springframework.context.event.EventListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/transaction")
public class TransactionController
{
    private ClustersContainer clustersContainer;
    private ApplicationEventPublisher eventPublisher;

    private UserService userService;
    private EhrDetailService ehrDetailService;
    private NotificationService notificationService;
    private ConsentRequestBlockService consentRequestService;

    private RsaUtil rsaUtil;
    private KeyUtil keyUtil;
    private UuidUtil uuidUtil;
    private ChainUtil chainUtil;
    private ModelMapper modelMapper;
    private ChainRootUtil chainRootUtil;
    private SimpleStringUtil simpleStringUtil;
    private BlockBroadcaster blockBroadcaster;


    @Autowired
    public TransactionController(ConsentRequestBlockService consentRequestService, UserService userService, EhrDetailService ehrDetailService, NotificationService notificationService, ChainRootUtil chainRootUtil, ClustersContainer clustersContainer, ApplicationEventPublisher eventPublisher, SimpleStringUtil simpleStringUtil, RsaUtil rsaUtil, KeyUtil keyUtil, UuidUtil uuidUtil, ChainUtil chainUtil, ModelMapper modelMapper, BlockBroadcaster blockBroadcaster) {
        this.eventPublisher = eventPublisher;
        this.clustersContainer = clustersContainer;
        this.userService = userService;
        this.ehrDetailService = ehrDetailService;
        this.notificationService = notificationService;
        this.consentRequestService = consentRequestService;
        this.rsaUtil = rsaUtil;
        this.keyUtil = keyUtil;
        this.uuidUtil = uuidUtil;
        this.chainUtil = chainUtil;
        this.modelMapper = modelMapper;
        this.chainRootUtil = chainRootUtil;
        this.simpleStringUtil = simpleStringUtil;
        this.blockBroadcaster = blockBroadcaster;
    }


    @PostMapping("/getConsent")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROVIDER')")
    public ResponseEntity getUserConsent(@RequestBody BlockAddition blockAddition)
    {
        String providerUUID = blockAddition.getProviderUUID();
        String networkUUID = blockAddition.getNetworkUUID();

        // The ID of the user to get consent from
        String userID = blockAddition.getEhrUserID();

        // Check if Provider UUID and User ID are valid
        if (!uuidUtil.isValidUUID(providerUUID) || !simpleStringUtil.isValidNumber(userID)) {
            return new ResponseEntity<>(
                new ApiResponse(false, "Invalid Provider UUID or User ID"),
                HttpStatus.BAD_REQUEST
            );
        }

        // Validate userID (exception thrown if not found)
        userService.findUserById(Long.parseLong(userID));

        // Check if provider exists in providers cluster
        if (!clustersContainer.getChainProviders().existsInCluster(providerUUID)) {
            return new ResponseEntity<>(
                new ApiResponse(false, "You are not in the providers list"),
                HttpStatus.BAD_REQUEST
            );
        }

        // Check if provider's network uuid is valid
        if (networkUUID == null || networkUUID.isEmpty()) {
            return new ResponseEntity<>(
                new ApiResponse(false, "Invalid provider network or doesn't exist"),
                HttpStatus.BAD_REQUEST
            );
        }

        // Get chain root from addition request payload
        String chainRoot = blockAddition.getChainRootWithoutBlock();

        /*
        *   Check if provider's chain is valid by Comparing sent chainRoot
        *   with the saved chainRoot (latest valid chain root)
        */

        boolean isValidNetworkChainRoot;

        // Check if sent root is valid
        try
        {
            isValidNetworkChainRoot = chainRootUtil.checkNetworkChainRoot(networkUUID, chainRoot);
        }
        catch (Exception Ex)
        {
            return new ResponseEntity<>(
                new ApiResponse(false, Ex.getMessage()),
                HttpStatus.BAD_REQUEST
            );
        }

        // If not valid
        if (!isValidNetworkChainRoot) {
            // Fetch the chain from another provider in network the specified network in the BlockAddition request
            // and send it to this node with invalid chain.

            try {
                chainUtil.fetchChainForNode(providerUUID, networkUUID);
            }
            catch (BadRequestException Ex) {
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

        // Send a consent request for adding the block to chain to user
        try
        {
            // Generate and construct a block using data in block addition request
            Block block = modelMapper.mapAdditionRequestToBlock(blockAddition);

            // Convert the block into a serializable block
            SerializableBlock sBlock = modelMapper.mapBlockToSerializableBlock(block);

            GetUserConsentEvent getUserConsent = new GetUserConsentEvent(
                this,
                sBlock,
                providerUUID,
                blockAddition.getNetworkUUID(),
                userID
            );

            eventPublisher.publishEvent(getUserConsent);
        }
        catch (Exception Ex)
        {
            if (Ex.getMessage().equals("User offline, a consent request notification was sent to user")) // Do you know any other way to do this ?
            {
                return new ResponseEntity<>(
                    new ApiResponse(true, Ex.getMessage()),
                    HttpStatus.OK
                );
            }

            return new ResponseEntity<>(
                new ApiResponse(false, Ex.getMessage()),
                HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
            new ApiResponse(true, "User consent request was successfully sent"),
            HttpStatus.ACCEPTED
        );
    }


    // Called when a user gives consent and accepts a consent request of a block addition
    @PostMapping("/give-consent")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity giveUserConsent(@RequestBody UserConsentResponse consentResponse) throws Exception
    {
        Block block;

        try {
            // Get Block from SerializableBlock
            block = modelMapper.mapSerializableBlockToBlock(consentResponse.getBlock());
        }
        catch (BadRequestException Ex) { // Catch mapping errors due to absence of any field
            return new ResponseEntity<>(
                new ApiResponse(false, "Invalid Block in Response, " + Ex.getMessage()),
                HttpStatus.BAD_REQUEST
            );
        }

        // Validate Consent Response.
        if (!isConsentResponseValid(consentResponse)) {
            return new ResponseEntity<>(
                new ApiResponse(false, "Provider has not made a consent request for this response"),
                HttpStatus.NOT_FOUND
            );
        }

        // Get user's EHR Details using their Address that was sent in the consent response
        EhrDetails ehrDetails;

        try {
            ehrDetails = ehrDetailService.findEhrDetails(consentResponse.getUserAddress());
        }
        catch (ResourceNotFoundException Ex) {
            return ResponseEntity.badRequest().body(
                new ApiResponse(false, "Invalid user address in consent response." + Ex.getMessage())
            );
        }

        // Get patient info from block
        PatientInfo patientInfo = block.getTransaction().getRecord().getPatientInfo();

        // Map EhrDetails data along with the patient info into a MedicalRecord
        MedicalRecord record = modelMapper.mapEhrDetailsToMedicalRecord(ehrDetails, patientInfo);

        // Add the MedicalRecord into the block
        block.getTransaction().setRecord(record);

        // Sign the block.
        SerializableBlock signedBlock = signBlock(consentResponse, block);


        try
        {
            // Broadcast the signed block to the other provider nodes in network.
            blockBroadcaster.broadcast(signedBlock, consentResponse.getNetworkUUID());
        }
        catch (ResourceEmptyException Ex) {
            return new ResponseEntity<>(
                new ApiResponse(false, Ex.getMessage()),
                HttpStatus.NOT_FOUND
            );
        }

        /*
        // Delete the consent request that matches the response data from DB
        deleteMatchingConsentRequest(consentResponse);

        // Change provider's network ChainRoot to the new sent chainRootWithBlock.
        //changeNetworkChainRoot(consentResponse.getNetworkUUID(), consentResponse.getChainRootWithBlock());
        */

        return new ResponseEntity<>(
            new ApiResponse(true, "Block has been signed and Broad-casted successfully"),
            HttpStatus.ACCEPTED
        );
    }

    @EventListener
    protected void getUserConsent(GetUserConsentEvent event)
    {
        String stringUserID = event.getUserID();
        Long userID;

        try {
            userID = Long.parseLong(stringUserID);
        }
        catch (Exception Ex) {
            // If user id is invalid or not numeric
            throw new BadRequestException("Invalid user ID");
        }

        // Check if user is registered/subscribed to app users cluster
        boolean isUserExists = clustersContainer.getAppUsers().existsInCluster(stringUserID);

        // If user doesn't exist in users cluster (user offline or wrong userID)
        // Save a ConsentRequestBlock notification to DB for user to check when they login
        if (!isUserExists)
        {
            // Create a consent request from block, user ID and provider UUID
            ConsentRequestBlock consentRequest = modelMapper.mapToConsentRequestBlock(
                userID,
                event.getProviderUUID(),
                event.getNetworkUUID(), // NetworkUUID of the provider
                event.getBlock()
            );

            // Persist the consent request
            consentRequestService.saveConsentRequest(consentRequest);

            // Send a notification to the user
            notificationService.notifyUser(consentRequest);

            throw new GeneralAppException("User offline, a consent request notification was sent to user");
        }

        // If user exists (online) then send it as a notification through a server sent event.

        UserConsentRequest userConsentRequest = new UserConsentRequest(
            event.getBlock(),
            event.getProviderUUID(),
            event.getNetworkUUID(),
            userID
        );

        // Get user emitter from users cluster
        SseEmitter userEmitter = clustersContainer.getAppUsers().getNodeEmitter(stringUserID);

        // Send the consent request with block data to user
        try {
            userEmitter.send(userConsentRequest, MediaType.APPLICATION_JSON);
        }
        catch (IOException Ex) {
            clustersContainer.getAppUsers().removeNode(stringUserID);
            throw new BadRequestException("An Error has occurred while sending SSE, user has been removed from AppUsers cluster.");
        }
    }

    private SerializableBlock signBlock(UserConsentResponse consentResponse, Block block) throws Exception
    {
        // Get Transaction from Block
        Transaction transaction = block.getTransaction();

        // Convert Base64 encoded String privateKey to PrivateKey
        PrivateKey privateKey = keyUtil.getPrivateKeyFromString(consentResponse.getUserPrivateKey());

        // Sign transaction and get signature
        byte[] signature = rsaUtil.rsaSign(privateKey, transaction);

        // Change the transaction signature
        transaction.setSignature(signature);

        // Change the block transaction with the transaction that has the signature
        block.setTransaction(transaction);

        // Get SerializableBlock from the signed block
        SerializableBlock serializableBlock = modelMapper.mapBlockToSerializableBlock(block);

        return serializableBlock;
    }

    private boolean isConsentResponseValid(UserConsentResponse consentResponse)
    {
        // check if the provider has sent such a block to the user before
        // or not, by checking the consent requests on DB made by the
        // provider to validate the response.

        boolean isResponseValid = false;

        // if a request that has transactionId identical to responseTransactionId is found in providerConsentRequests
        if (findMatchingConsentRequest(consentResponse) != null)
        {
            isResponseValid = true;
        }

        return isResponseValid;
    }

    private ConsentRequestBlock findMatchingConsentRequest(UserConsentResponse consentResponse)
    {
        // Goes through the provider consent requests and checks if the hash of the transaction(transactionID)
        // in the consent response equals any of the hashes of the provider requests to validate if the
        // consent request that the user responded to is valid and was made by the provider or not.

        // Provider UUID from consent response
        String responseProviderUUID = consentResponse.getProviderUUID();

        // Get the list of Consent requests made by that provider UUID
        List<ConsentRequestBlock> providerConsentRequestsList = consentRequestService.findRequestsByProvider(responseProviderUUID);

        // If the provider has any open(unanswered) consent requests
        if (providerConsentRequestsList != null)
        {
            ArrayList<ConsentRequestBlock> providerConsentRequests = new ArrayList<>(
                    providerConsentRequestsList
            );

            String responseTransactionId = consentResponse.getBlock().getTransaction().getTransactionId();

            // if a request that has transactionId identical to responseTransactionId is found in providerConsentRequests
            for (ConsentRequestBlock request : providerConsentRequests) {
                if (request.getTransactionId().equals(responseTransactionId)) {
                    // return that request
                    return request;
                }
            }
        }

        return null;
    }

    private void deleteMatchingConsentRequest(UserConsentResponse consentResponse)
    {
        ConsentRequestBlock consentRequest = findMatchingConsentRequest(consentResponse);

        if (consentRequest != null)
        {
            // delete consentRequest from DB
            consentRequestService.deleteRequest(consentRequest);
        }

    }

    private void changeNetworkChainRoot(String networkUUID, String chainRootWithBlock)
    {

        /*
        Todo------------------------------------------------------------------|
            Get networkUUID from NetworkService in DB by a query that finds
            networkUUID of a provider since providersUUIDs are going to be
            saved in networks in DB.
        Todo------------------------------------------------------------------|
        */

        // Change the provider's network chain root
        chainRootUtil.changeNetworkChainRoot(networkUUID, chainRootWithBlock);
    }
}
