package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Core.GenesisBlock;
import com.project.EhrRoute.Core.Utilities.StringUtil;
import com.project.EhrRoute.Entities.App.NetworkInvitationRequest;
import com.project.EhrRoute.Entities.App.Notification;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Entities.Auth.VerificationToken;
import com.project.EhrRoute.Entities.Core.ChainRoot;
import com.project.EhrRoute.Entities.Core.ConsentRequestBlock;
import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Entities.Core.UpdateConsentRequest;
import com.project.EhrRoute.Exceptions.NullUserNetworkException;
import com.project.EhrRoute.Exceptions.ResourceEmptyException;
import com.project.EhrRoute.Models.NotificationType;
import com.project.EhrRoute.Payload.App.NetworkDetails;
import com.project.EhrRoute.Payload.App.NetworkInvitationRequestPayload;
import com.project.EhrRoute.Payload.App.NetworkRootUpdate;
import com.project.EhrRoute.Payload.App.SimpleStringPayload;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Payload.Core.SerializableBlock;
import com.project.EhrRoute.Security.CurrentUser;
import com.project.EhrRoute.Security.UserPrincipal;
import com.project.EhrRoute.Services.*;
import com.project.EhrRoute.Utilities.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.Calendar;
import java.util.List;


@RestController
@RequestMapping("/network")
public class NetworkController
{
    private NetworkInvitationRequestService invitationRequestService;
    private UpdateConsentRequestService updateConsentRequestService;
    private VerificationTokenService verificationTokenService;
    private ConsentRequestBlockService consentRequestService;
    private NotificationService notificationService;
    private ChainRootService chainRootService;
    private NetworkService networkService;
    private UserService userService;

    private GenesisBlock genesisBlock;

    private ModelMapper modelMapper;
    private StringUtil stringUtil;


    @Autowired
    public NetworkController(NetworkInvitationRequestService invitationRequestService, UpdateConsentRequestService updateConsentRequestService, VerificationTokenService verificationTokenService, ConsentRequestBlockService consentRequestService, NotificationService notificationService, ChainRootService chainRootService, NetworkService networkService, UserService userService, GenesisBlock genesisBlock, ModelMapper modelMapper, StringUtil stringUtil) {
        this.updateConsentRequestService = updateConsentRequestService;
        this.invitationRequestService = invitationRequestService;
        this.verificationTokenService = verificationTokenService;
        this.consentRequestService = consentRequestService;
        this.notificationService = notificationService;
        this.chainRootService = chainRootService;
        this.networkService = networkService;
        this.userService = userService;
        this.genesisBlock = genesisBlock;
        this.modelMapper = modelMapper;
        this.stringUtil = stringUtil;
    }


    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROVIDER')")
    public ResponseEntity createNetwork(@RequestBody String networkName, @CurrentUser UserPrincipal currentUser) throws Exception
    {
        if (currentUser == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User not logged in"));
        }

        // Get current logged in user
        User user = userService.findUserByUsernameOrEmail(currentUser.getUsername());

        if (user == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User not found"));
        }

        // Initialize genesis block, a random network UUID is generated for every call,
        // thus the hash of the block will also change.
        genesisBlock.initializeBlock();

        // A networkUUID is generated in every genesis block.
        String networkUUID = genesisBlock.getBlock().getBlockHeader().getNetworkUUID();

        // Chain has only one block, so the merkle root is set to the first and only leaf hash of the tree
        String chainRoot = stringUtil.base64EncodeBytes(genesisBlock.getBlock().getBlockHeader().getMerkleLeafHash());

        // Generate a new network using the genesis block data
        Network network = new Network();
        network.setNetworkUUID(networkUUID);
        network.setName(networkName);

        ChainRoot networkChainRoot = new ChainRoot(chainRoot);
        network.setChainRoot(networkChainRoot);

        // Save network in DB
        networkService.saveNetwork(network);

        // Add network to user's networks
        user.addNetwork(network);
        userService.saveUser(user);

        // Genesis block to be returned
        SerializableBlock genesis = modelMapper.mapBlockToSerializableBlock(genesisBlock.getBlock());

        return ResponseEntity.ok(genesis);
    }

    /**
     * Fetches a network merkle root given the network's UUID
     * @param networkUUID   the network UUID
     * @return              the network's merkle root
     */
    @GetMapping("/merkle-root")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROVIDER')")
    public ResponseEntity getNetworkChainRoot(@RequestParam("networkuuid") String networkUUID)
    {
        Network network = networkService.findNetwork(networkUUID);

        return ResponseEntity.ok(new SimpleStringPayload(network.getChainRoot().getRoot()));
    }


    /**
     * Finalizes the transaction by updating the network's merkle root and deleting the consent request on DB
     * @param networkRootUpdate     the merkle root update request data
     * @return                      HTTP 200 on success
     */
    @PostMapping("/merkle-root")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity updateNetworkMerkleRoot(@RequestBody NetworkRootUpdate networkRootUpdate)
    {
        // Get and validate the request by checking if there is a consent request with the UUID sent on the update request
        ConsentRequestBlock consentRequest = consentRequestService.findConsentRequest(networkRootUpdate.getConsentRequestUUID());

        // Change the network's root
        chainRootService.changeNetworkChainRoot(networkRootUpdate.getNetworkUUID(), networkRootUpdate.getMerkleRoot());

        try {
            // Delete the consent request
            consentRequestService.deleteRequest(consentRequest);
        }
        catch (Exception Ex) {
            // If consent request wasn't deleted because there is an update consent request foreign key constraint
            // then find and delete the update consent request that has/mapped-to this consent request
            UpdateConsentRequest updateConsentRequest = updateConsentRequestService.findUpdateConsentRequest(consentRequest);
            updateConsentRequestService.deleteUpdateConsentRequest(updateConsentRequest);
        }

        return ResponseEntity.ok(new ApiResponse(true, "Network's merkle root has been updated successfully"));
    }


    @GetMapping("/uuid")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROVIDER')")
    public ResponseEntity getNetworkUUID(@RequestParam("name") String networkName)
    {
        String networkUUID;

        // Validate network with requested name existence, and get its UUID
        try {
            networkUUID = networkService.getNetworkUuidByName(networkName);
        }
        catch (NullUserNetworkException Ex) {
            return new ResponseEntity<>(
                new ApiResponse(false, Ex.getMessage()),
                HttpStatus.BAD_REQUEST
            );
        }

        System.out.println(networkUUID);

        return new ResponseEntity<>(
            new SimpleStringPayload(networkUUID),
            HttpStatus.OK
        );
    }


    @GetMapping("/details")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROVIDER') or hasRole('USER')")
    public ResponseEntity getNetworkDetails(@RequestParam("networkuuid") String networkUUID)
    {
        NetworkDetails networkDetails;

        try {
            networkDetails = networkService.getNetworkDetails(networkUUID);
        }
        catch (Exception Ex) {
            return ResponseEntity.badRequest().body(
                new ApiResponse(false, Ex.getMessage())
            );
        }

        return ResponseEntity.ok(
            networkDetails
        );
    }


    @GetMapping("/search-by-name")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROVIDER')")
    public List<String> searchNetworksByNetworkNames(@RequestParam("keyword") String networkName)
    {
        return networkService.searchNetworksByName(networkName);
    }


    @PostMapping("/invite")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROVIDER')")
    public ResponseEntity sendNetworkInvitationRequest(@RequestBody NetworkInvitationRequestPayload invitationRequest)
    {
        // Get the invitation recipient and sender from NetworkInvitationRequestPayload data
        User recipient = userService.findUserByUsernameOrEmail(invitationRequest.getRecipientUsername());
        User sender = userService.findUserByUsernameOrEmail(invitationRequest.getSenderUsername());
        Network network = networkService.findNetwork(invitationRequest.getNetworkUUID());

        // Validate recipient
        if (recipient == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid recipient username on invitation request"));
        }

        // Validate sender
        if (sender == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid sender username on invitation request"));
        }

        // Check if recipient is already on network
        if (userService.userHasNetwork(recipient, network)) {
            return new ResponseEntity<>(new ApiResponse(false, "User is already on the network"), HttpStatus.CONFLICT);
        }

        // Create a Notification object
        Notification notification = new Notification();

        // Generate(and save) a NetworkInvitationRequest using NetworkInvitationRequestService
        NetworkInvitationRequest networkInvitationRequest = invitationRequestService.generateInvitationRequest(
            recipient,
            invitationRequest.getSenderUsername(),
            invitationRequest.getNetworkName(),
            invitationRequest.getNetworkUUID()
        );

        // Add the generated NetworkInvitationRequest to Notification object as reference
        notification.setReference(networkInvitationRequest);

        // Set notification data
        notification.setRecipient(recipient);
        notification.setSender(sender);
        notification.setType(NotificationType.NETWORK_INVITATION);

        // Persist notification
        notificationService.saveNotification(notification);

        return ResponseEntity.ok(new ApiResponse(true, "A network invitation request notification has been successfully sent"));
    }


    @PostMapping("/invitation-accept")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROVIDER')")
    public ResponseEntity acceptNetworkInvitationRequest(@RequestBody NetworkInvitationRequestPayload invitationResponse)
    {
        NetworkInvitationRequest invitationRequest;

        try
        {
            // Get NetworkInvitationRequest object from invitation response
            invitationRequest = modelMapper.mapInvitationResponseToRequest(invitationResponse);
        }
        catch (ResourceEmptyException Ex) // Catch null or empty response fields
        {
            return new ResponseEntity<>(
                new ApiResponse(true, "Invalid invitation response object"),
                HttpStatus.BAD_REQUEST
            );
        }

        // Validate invitation request of response
        boolean requestExists = invitationRequestService.validateInvitationRequestExistence(invitationRequest);

        /*
        *   Verify token in response and its expiration date -
        *   (by getting token from DB using tokenService and checking if expired)
        *   after validating the response existence
        */

        // If a request with the response values exist on DB
        if (requestExists)
        {
            // Get current calendar time(used to check if invitation token is expired)
            Calendar cal = Calendar.getInstance();

            // Get token object from DB using the token string taken from url.
            VerificationToken token = verificationTokenService.getVerificationToken(invitationResponse.getInvitationToken());

            // Invalid token check
            if (token == null)
            {
                return new ResponseEntity<>(
                    new ApiResponse(false, "Invalid invitation token in response"),
                    HttpStatus.BAD_REQUEST
                );
            }

            // Expired token check
            if ((token.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0)
            {
                return new ResponseEntity<>(
                    new ApiResponse(false, "Expired invitation request"),
                    HttpStatus.BAD_REQUEST
                );
            }

            // Add Network to User networks if token is valid (not expired and exists)
            try
            {
                addUserNetwork(invitationResponse.getRecipientUsername(), invitationResponse.getNetworkUUID());
            }
            catch (Exception Ex)
            {
                // If a user with the recipient name of response was not found
                if (Ex.getClass().equals(UsernameNotFoundException.class))
                {
                    return new ResponseEntity<>(
                        new ApiResponse(false, Ex.getMessage()),
                        HttpStatus.BAD_REQUEST
                    );
                }

                // If a network with the network UUID of response was not found
                if (Ex.getClass().equals(NullUserNetworkException.class))
                {
                    return new ResponseEntity<>(
                        new ApiResponse(false, Ex.getMessage()),
                        HttpStatus.BAD_REQUEST
                    );
                }
            }

        }
        // If a request with the response values doesn't exist on DB
        else
        {
            return new ResponseEntity<>(
                new ApiResponse(true, "An invitation request wasn't sent with such invitation response values. Invalid invitation."),
                HttpStatus.BAD_REQUEST
            );
        }

        // If no error occurred, and everything was valid, send an OK response.
        return new ResponseEntity<>(
            new ApiResponse(true, "An invitation request wasn't sent with such invitation response values. Invalid invitation."),
            HttpStatus.OK
        );
    }

    private void addUserNetwork(String invitedUserUsername, String networkUUID) {

        // Get the invited User object
        User invitedUser = userService.findUserByUsernameOrEmail(invitedUserUsername);

        // Handle user not found on DB
        if (invitedUser == null) {
            throw new UsernameNotFoundException("A user with username: " + invitedUserUsername + " was not found. Adding network to user networks failed.");
        }

        // Get the network of networkUUID
        Network network = networkService.findNetwork(networkUUID);

        // Add network to user networks if no error occurred
        invitedUser.addNetwork(network);

        // Save user to persist changes
        userService.saveUser(invitedUser);
    }
}
