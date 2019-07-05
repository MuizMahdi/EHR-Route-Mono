package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Core.Block;
import com.project.EhrRoute.Core.BlockTransmitter;
import com.project.EhrRoute.Core.Transaction;
import com.project.EhrRoute.Core.Utilities.HashUtil;
import com.project.EhrRoute.Core.Utilities.KeyUtil;
import com.project.EhrRoute.Core.Utilities.RsaUtil;
import com.project.EhrRoute.Models.BlockSource;
import com.project.EhrRoute.Models.UuidSourceType;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Payload.Core.*;
import com.project.EhrRoute.Payload.Core.SSEs.BlockMetadata;
import com.project.EhrRoute.Payload.Core.SSEs.BlockResponse;
import com.project.EhrRoute.Services.*;
import com.project.EhrRoute.Utilities.ModelMapper;
import com.project.EhrRoute.Utilities.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.PrivateKey;


@RestController
@RequestMapping("/transaction")
public class TransactionController
{
    private UserService userService;
    private TransactionService transactionService;
    private ConsentRequestBlockService consentRequestService;
    private RsaUtil rsaUtil;
    private KeyUtil keyUtil;
    private HashUtil hashUtil;
    private UuidUtil uuidUtil;
    private ModelMapper modelMapper;
    private BlockTransmitter blockTransmitter;


    @Autowired
    public TransactionController(ConsentRequestBlockService consentRequestService, UserService userService, TransactionService transactionService, RsaUtil rsaUtil, KeyUtil keyUtil, HashUtil hashUtil, UuidUtil uuidUtil, ModelMapper modelMapper, BlockTransmitter blockTransmitter) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.consentRequestService = consentRequestService;
        this.rsaUtil = rsaUtil;
        this.keyUtil = keyUtil;
        this.hashUtil = hashUtil;
        this.uuidUtil = uuidUtil;
        this.modelMapper = modelMapper;
        this.blockTransmitter = blockTransmitter;
    }


    @PostMapping("/consent-request")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity getUserConsent(@RequestBody BlockAddition blockAddition)
    {
        /*
        String providerUUID = blockAddition.getProviderUUID();
        String networkUUID = blockAddition.getNetworkUUID();
        String userID = blockAddition.getEhrUserID(); // The ID of the user to get consent from

        // Validate provider and network UUID
        uuidUtil.validateResourceUUID(providerUUID, UuidSourceType.PROVIDER);
        uuidUtil.validateResourceUUID(networkUUID, UuidSourceType.NETWORK);

        // Validate user
        userService.findUserById(Long.parseLong(userID));

        // Check if provider exists in providers cluster
        if (!clustersContainer.getChainProviders().existsInCluster(providerUUID)) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "You are not in the providers list"));
        }

        // Get chain root from addition request payload
        String chainRoot = blockAddition.getChainRootWithoutBlock();

        // Check if provider's chain is valid by Comparing sent chainRoot with the latest valid saved chain root
        boolean isValidNetworkChainRoot = chainRootService.checkNetworkChainRoot(networkUUID, chainRoot);

        // If not valid
        if (!isValidNetworkChainRoot) {
            // Fetch the chain from another provider in network the specified network in the BlockAddition request
            // and send it to this node with invalid chain.
            try {
                chainUtil.fetchChainForNode(providerUUID, networkUUID);
            }
            catch (BadRequestException Ex) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid Chain Root or Bad Chain. Chain fetching failed, caused by: " + Ex.getMessage()));
            }

            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid Chain Root or Bad Chain, chain fetching request was sent."));
        }

        // Send a consent request for adding the block to chain to user
        try {
            // Generate and construct a block using the data in the block addition request
            Block block = modelMapper.mapAdditionRequestToBlock(blockAddition);

            // Convert the block into a serializable block
            SerializableBlock sBlock = modelMapper.mapBlockToSerializableBlock(block);

            // Construct and publish a GetUserConsent event
            GetUserConsentEvent getUserConsent = new GetUserConsentEvent(this, sBlock, providerUUID, blockAddition.getNetworkUUID(), userID);

            eventPublisher.publishEvent(getUserConsent);
        }
        catch (Exception Ex) {
            if (Ex.getMessage().equals("User offline, a consent request notification was sent to user")) {
                return ResponseEntity.ok(new ApiResponse(true, Ex.getMessage()));
            }

            return ResponseEntity.badRequest().body(new ApiResponse(false, Ex.getMessage()));
        }
        */
        transactionService.getUserConsent(blockAddition);
        return ResponseEntity.accepted().body(new ApiResponse(true, "Consent request was sent"));
    }


    @PostMapping("/get-update-consent")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity getUserUpdateConsent(@RequestBody UpdatedBlockAddition updatedBlockAddition)
    {
        String providerUUID = updatedBlockAddition.getBlockAddition().getProviderUUID();
        String networkUUID = updatedBlockAddition.getBlockAddition().getNetworkUUID();
        String userID = updatedBlockAddition.getBlockAddition().getEhrUserID(); // ID of the user to request their consent

        // Validate provider and network UUID
        uuidUtil.validateResourceUUID(providerUUID, UuidSourceType.PROVIDER);
        uuidUtil.validateResourceUUID(networkUUID, UuidSourceType.NETWORK);

        // Validate user
        userService.findUserById(Long.parseLong(userID));

        // Send a notification
        transactionService.sendUpdateConsentRequest(updatedBlockAddition.getBlockAddition(), updatedBlockAddition.getRecordUpdateData());

        return ResponseEntity.accepted().build();
    }


    // Called when a user gives consent and accepts a consent request of a block addition
    @PostMapping("/give-consent")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity giveUserConsent(@RequestBody UserConsentResponse consentResponse) throws Exception
    {
        // Validate Consent Response.
        if (!consentRequestService.isConsentResponseValid(consentResponse)) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "A consent request has not been made for this response"));
        }

        // TODO: SOLVE ISSUE: Serializing a JSON object into a Java Map (history in medical record serialization error)

        // Map the SerializableBlock in response into a Block
        Block block = modelMapper.mapSerializableBlockToBlock(consentResponse.getBlock());

        // Sign the block.
        block = signBlock(consentResponse, block);

        // Update the block
        block = updateBlockLeafHash(block);

        // Construct a block addition response
        BlockResponse blockResponse = new BlockResponse(
            modelMapper.mapBlockToSerializableBlock(block), // Get SerializableBlock from the updated block
            new BlockMetadata(consentResponse.getConsentRequestUUID(), BlockSource.BROADCAST.toString()) // Create block metadata
        );

        // Broadcast the block response to the providers (nodes) of the network.
        blockTransmitter.broadcast(blockResponse);

        return ResponseEntity.accepted().body(new ApiResponse(true, "Block has been signed and Broadcasted successfully"));
    }


    @PostMapping("/give-update-consent")
    public ResponseEntity giveUserUpdateConsent(@RequestBody UserUpdateConsentResponse updateConsentResponse) throws Exception
    {
        // Validate the consent request that the user responded to
        if (!consentRequestService.isConsentResponseValid(updateConsentResponse.getConsentResponse())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Provider has not made a consent request for this response"));
        }

        // Get Block from SerializableBlock in the consent request of the update consent response
        Block block = modelMapper.mapSerializableBlockToBlock(updateConsentResponse.getConsentResponse().getBlock());

        // Sign and update the block.
        block = signBlock(updateConsentResponse.getConsentResponse(), block);
        block = updateBlockLeafHash(block);

        // Construct a block addition response
        BlockResponse blockResponse = new BlockResponse(
            modelMapper.mapBlockToSerializableBlock(block),
            new BlockMetadata(updateConsentResponse.getConsentResponse().getConsentRequestUUID(), BlockSource.BROADCAST.toString())
        );

        // Broadcast the signed block to the other provider nodes in network.
        blockTransmitter.broadcast(blockResponse);

        return ResponseEntity.accepted().body(new ApiResponse(true, "Updated block has been signed and Broadcasted successfully"));
    }

/*
    @EventListener
    protected void getUserConsent(GetUserConsentEvent event) {

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
        if (!isUserExists) {
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
    }
*/

    private Block signBlock(UserConsentResponse consentResponse, Block block) throws Exception {

        // Get Transaction from Block
        Transaction transaction = block.getTransaction();

        // Convert Base64 encoded String privateKey to PrivateKey
        PrivateKey privateKey = keyUtil.getPrivateKeyFromString(consentResponse.getUserPrivateKey());

        // Sign transaction and get signature
        byte[] signature = rsaUtil.rsaSign(privateKey, transaction);

        // Update the transaction signature
        transaction.setSignature(signature);

        // Update the block's transaction with the signed transaction
        block.setTransaction(transaction);

        return block;
    }

    private Block updateBlockLeafHash(Block block) {

        // Update the TxID
        block.getTransaction().setTransactionId(
            hashUtil.hashTransactionData(block.getTransaction())
        );

        // Update the merkle leaf hash
        block.getBlockHeader().setMerkleLeafHash(
            hashUtil.SHA256(block.getTransaction().getTransactionId())
        );

        return block;
    }
}
