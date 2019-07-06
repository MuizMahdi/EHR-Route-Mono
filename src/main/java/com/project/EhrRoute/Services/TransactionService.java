package com.project.EhrRoute.Services;
import com.project.EhrRoute.Core.Block;
import com.project.EhrRoute.Core.BlockTransmitter;
import com.project.EhrRoute.Core.Utilities.BlockUtil;
import com.project.EhrRoute.Entities.Core.ConsentRequestBlock;
import com.project.EhrRoute.Entities.Core.UpdateConsentRequest;
import com.project.EhrRoute.Entities.EHR.*;
import com.project.EhrRoute.Exceptions.BadRequestException;
import com.project.EhrRoute.Models.UuidSourceType;
import com.project.EhrRoute.Payload.Core.*;
import com.project.EhrRoute.Payload.Core.SSEs.BlockMetadata;
import com.project.EhrRoute.Payload.Core.SSEs.BlockResponse;
import com.project.EhrRoute.Payload.EHR.RecordUpdateData;
import com.project.EhrRoute.Utilities.ModelMapper;
import com.project.EhrRoute.Utilities.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;


@Service
public class TransactionService
{
    private ModelMapper modelMapper;
    private UpdateConsentRequestService updateConsentRequestService;
    private ConsentRequestBlockService consentRequestService;
    private NotificationService notificationService;
    private ChainRootService chainRootService;
    private EhrDetailService ehrDetailService;
    private UserService userService;
    private BlockTransmitter blockTransmitter;
    private BlockUtil blockUtil;
    private UuidUtil uuidUtil;


    @Autowired
    public TransactionService(ModelMapper modelMapper, UpdateConsentRequestService updateConsentRequestService, ConsentRequestBlockService consentRequestService, NotificationService notificationService, ChainRootService chainRootService, EhrDetailService ehrDetailService, UserService userService, BlockTransmitter blockTransmitter, BlockUtil blockUtil, UuidUtil uuidUtil) {
        this.modelMapper = modelMapper;
        this.updateConsentRequestService = updateConsentRequestService;
        this.consentRequestService = consentRequestService;
        this.notificationService = notificationService;
        this.chainRootService = chainRootService;
        this.ehrDetailService = ehrDetailService;
        this.userService = userService;
        this.blockTransmitter = blockTransmitter;
        this.blockUtil = blockUtil;
        this.uuidUtil = uuidUtil;
    }


    public void getUserConsent(BlockAddition blockAdditionRequest) {
        String providerUUID = blockAdditionRequest.getProviderUUID(); // The node UUID
        String networkUUID = blockAdditionRequest.getNetworkUUID();
        String userID = blockAdditionRequest.getEhrUserID(); // ID of the user to get consent from
        String chainRoot = blockAdditionRequest.getChainRootWithoutBlock(); // The node's local chain merkle root

        // Validate UUIDs
        uuidUtil.validateResourceUUID(providerUUID, UuidSourceType.PROVIDER);
        uuidUtil.validateResourceUUID(networkUUID, UuidSourceType.NETWORK);

        // Validate user
        userService.findUserById(Long.parseLong(userID));

        // Validate node's chain
        if (!chainRootService.isChainRootValid(networkUUID, chainRoot)) {
            // TODO: Check if node has latest chain blocks,
            // TODO: If blocks are missing then create a fetch request for the missing blocks;
            // TODO: If all blocks are present then create a fetch request for the whole chain
        }

        // Generate and construct a block using the data in the block addition request
        Block block = modelMapper.mapAdditionRequestToBlock(blockAdditionRequest);
        SerializableBlock serializableBlock = modelMapper.mapBlockToSerializableBlock(block);

        sendConsentRequest(serializableBlock, providerUUID, networkUUID, Long.parseLong(userID));
    }


    public void giveUserConsent(UserConsentResponse consentResponse) {
        // Validate Consent Response.
        if (!consentRequestService.isConsentResponseValid(consentResponse)) {
            throw new BadRequestException("Invalid consent response");
        }

        // TODO: SOLVE ISSUE: Serializing a JSON object into a Java Map (history in medical record serialization error)

        // Map the SerializableBlock in response into a Block
        Block block = modelMapper.mapSerializableBlockToBlock(consentResponse.getBlock());

        // Sign the block.
        try {
            block = blockUtil.signBlock(consentResponse.getUserPrivateKey(), block);
        }
        catch (Exception Ex) {
            throw new BadRequestException("Invalid private key");
        }

        // Update the block
        block = blockUtil.updateBlockLeafHash(block);

        // Construct a block addition response
        BlockResponse blockResponse = new BlockResponse(
            modelMapper.mapBlockToSerializableBlock(block), // Get SerializableBlock from the updated block
            new BlockMetadata(consentResponse.getConsentRequestUUID()) // Create block metadata
        );

        // Broadcast the block response to the providers (nodes) of the network.
        blockTransmitter.broadcast(blockResponse);
    }


    private void sendConsentRequest(SerializableBlock block, String providerUUID, String networkUUID, Long userId) {
        // Create a consent request
        ConsentRequestBlock consentRequest = modelMapper.mapToConsentRequestBlock(userId, providerUUID, networkUUID, block);
        // Persist the consent request
        consentRequestService.saveConsentRequest(consentRequest);
        // Send a notification to the user
        notificationService.notifyUser(consentRequest);
    }


    public void getUserUpdateConsent(UpdatedBlockAddition updatedBlockAddition) {
        String providerUUID = updatedBlockAddition.getBlockAddition().getProviderUUID();
        String networkUUID = updatedBlockAddition.getBlockAddition().getNetworkUUID();
        String userID = updatedBlockAddition.getBlockAddition().getEhrUserID();
        String chainRoot = updatedBlockAddition.getBlockAddition().getChainRootWithoutBlock(); // The node's local chain merkle root

        uuidUtil.validateResourceUUID(providerUUID, UuidSourceType.PROVIDER);
        uuidUtil.validateResourceUUID(networkUUID, UuidSourceType.NETWORK);

        // Validate user
        userService.findUserById(Long.parseLong(userID));

        // Validate node's chain
        if (!chainRootService.isChainRootValid(networkUUID, chainRoot)) {
            // TODO
        }

        // Generate and construct a block using the data in the block addition request
        Block block = modelMapper.mapAdditionRequestToBlock(updatedBlockAddition.getBlockAddition());
        SerializableBlock serializableBlock = modelMapper.mapBlockToSerializableBlock(block);

        // Send a notification
        sendUpdateConsentRequest(serializableBlock, updatedBlockAddition.getRecordUpdateData(), providerUUID, networkUUID, Long.parseLong(userID));
    }


    public void giveUserUpdateConsent(UserUpdateConsentResponse updateConsentResponse) {
        // Validate the consent request that the user responded to
        if (!consentRequestService.isConsentResponseValid(updateConsentResponse.getConsentResponse())) {
            throw new BadRequestException("Invalid update consent response");
        }

        // Get Block from SerializableBlock in the consent request of the update consent response
        Block block = modelMapper.mapSerializableBlockToBlock(updateConsentResponse.getConsentResponse().getBlock());

        // Sign the block
        try {
            block = blockUtil.signBlock(updateConsentResponse.getConsentResponse().getUserPrivateKey(), block);
        }
        catch (Exception Ex) {
            throw new BadRequestException("Invalid private key");
        }

        // Update the block
        block = blockUtil.updateBlockLeafHash(block);

        // Construct a block addition response
        BlockResponse blockResponse = new BlockResponse(
            modelMapper.mapBlockToSerializableBlock(block),
            new BlockMetadata(updateConsentResponse.getConsentResponse().getConsentRequestUUID())
        );

        // Broadcast the signed block to the other provider nodes in network.
        blockTransmitter.broadcast(blockResponse);
    }


    private void sendUpdateConsentRequest(SerializableBlock block, RecordUpdateData recordUpdateData, String providerUUID, String networkUUID, Long userId) {
        // Create a consent request from block, user ID and provider UUID
        ConsentRequestBlock consentRequest = modelMapper.mapToConsentRequestBlock(userId, providerUUID, networkUUID, block);

        // Persist the consent request
        consentRequestService.saveConsentRequest(consentRequest);

        /* Generate an EHR Detail and add the EHR proposed update data into it.
        This will be used as a temp EhrDetail until the user accepts or rejects the request. */
        EhrDetails ehrDetails = new EhrDetails();

        /* Get the EHR Data of the update consent request from the event */
        recordUpdateData.getConditions().forEach(medicalProblem -> {
            EhrProblems ehrProblem = new EhrProblems(medicalProblem);
            ehrDetails.addProblem(ehrProblem);
        });

        recordUpdateData.getAllergies().forEach(allergy -> {
            EhrAllergies ehrAllergy = new EhrAllergies(allergy);
            ehrDetails.addAllergy(ehrAllergy);
        });

        // Persist changes
        ehrDetailService.saveEhrDetails(ehrDetails);

        // Create and save an UpdateConsentRequest
        UpdateConsentRequest updateConsentRequest = updateConsentRequestService.createUpdateConsentRequest(consentRequest, ehrDetails);

        // Send a notification to the user
        notificationService.notifyUser(consentRequest, updateConsentRequest);
    }
}
