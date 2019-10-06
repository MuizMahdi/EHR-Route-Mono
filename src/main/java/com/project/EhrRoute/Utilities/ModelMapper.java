package com.project.EhrRoute.Utilities;
import com.project.EhrRoute.Core.*;
import com.project.EhrRoute.Core.Utilities.HashUtil;
import com.project.EhrRoute.Core.Utilities.KeyUtil;
import com.project.EhrRoute.Core.Utilities.StringUtil;
import com.project.EhrRoute.Entities.App.NetworkInvitationRequest;
import com.project.EhrRoute.Entities.App.Notification;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Entities.Core.ConsentRequestBlock;
import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Entities.EHR.*;
import com.project.EhrRoute.Exceptions.NullUserNetworkException;
import com.project.EhrRoute.Exceptions.ResourceEmptyException;
import com.project.EhrRoute.Payload.App.NetworkInvitationRequestPayload;
import com.project.EhrRoute.Payload.App.NotificationResponse;
import com.project.EhrRoute.Payload.Core.*;
import com.project.EhrRoute.Services.NetworkService;
import com.project.EhrRoute.Services.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class ModelMapper
{

    private KeyUtil keyUtil;
    private UuidUtil uuidUtil;
    private HashUtil hashUtil;
    private StringUtil stringUtil;
    private BlockHeader blockHeader;
    private Transaction transaction;
    private NetworkService networkService;
    private ProviderService providerService;


    @Autowired
    public ModelMapper(KeyUtil keyUtil, UuidUtil uuidUtil, HashUtil hashUtil, StringUtil stringUtil, BlockHeader blockHeader, Transaction transaction, NetworkService networkService, ProviderService providerService) {
        this.keyUtil = keyUtil;
        this.uuidUtil = uuidUtil;
        this.hashUtil = hashUtil;
        this.stringUtil = stringUtil;
        this.blockHeader = blockHeader;
        this.transaction = transaction;
        this.networkService = networkService;
        this.providerService = providerService;
    }


    public SerializableBlock mapBlockToSerializableBlock(Block block)
    {
        /*                                                            */
        // ||||| Populate the SerializableBlock with Block data ||||| //
        /*                                                            */

        SerializableBlock serializableBlock = new SerializableBlock();

        SerializableBlockHeader serializableBlockHeader = new SerializableBlockHeader();
        SerializableTransaction serializableTransaction = new SerializableTransaction();

        serializableBlockHeader.setHash(stringUtil.base64EncodeBytes(block.getBlockHeader().getHash()));
        serializableBlockHeader.setPreviousHash(stringUtil.base64EncodeBytes(block.getBlockHeader().getPreviousHash()));
        serializableBlockHeader.setTimeStamp(block.getBlockHeader().getTimeStamp());
        serializableBlockHeader.setIndex(block.getBlockHeader().getIndex());
        serializableBlockHeader.setMerkleLeafHash(stringUtil.base64EncodeBytes(block.getBlockHeader().getMerkleLeafHash()));
        serializableBlockHeader.setNetworkUUID(block.getBlockHeader().getNetworkUUID());

        serializableBlock.setBlockHeader(serializableBlockHeader);

        byte[] blockTxID = block.getTransaction().getTransactionId();
        serializableTransaction.setTransactionId(stringUtil.base64EncodeBytes(blockTxID));
        serializableTransaction.setRecord(block.getTransaction().getRecord());

        serializableTransaction.setSenderAddress(block.getTransaction().getSenderAddress().getAddress());
        serializableTransaction.setRecipientAddress(block.getTransaction().getRecipientAddress().getAddress());

        byte[] signatureBytes = block.getTransaction().getSignature();
        serializableTransaction.setSignature(stringUtil.base64EncodeBytes(signatureBytes));

        serializableBlock.setTransaction(serializableTransaction);

        return serializableBlock;
    }


    public Block mapSerializableBlockToBlock(SerializableBlock serializableBlock)
    {
        Block block = new Block();

        //BlockHeader blockHeader = new BlockHeader();

        blockHeader.setIndex(serializableBlock.getBlockHeader().getIndex());
        blockHeader.setTimeStamp(serializableBlock.getBlockHeader().getTimeStamp());

        String stringHash = serializableBlock.getBlockHeader().getHash();
        blockHeader.setHash(stringUtil.base64DecodeString(stringHash));

        String stringPreviousHash = serializableBlock.getBlockHeader().getPreviousHash();
        blockHeader.setPreviousHash(stringUtil.base64DecodeString(stringPreviousHash));

        String stringMerkleLeafHash = serializableBlock.getBlockHeader().getMerkleLeafHash();
        blockHeader.setMerkleLeafHash(stringUtil.base64DecodeString(stringMerkleLeafHash));

        String networkUUID = serializableBlock.getBlockHeader().getNetworkUUID();
        blockHeader.setNetworkUUID(networkUUID);

        block.setBlockHeader(blockHeader);

        //Transaction transaction = new Transaction();

        String base64EncodedStringSignature = serializableBlock.getTransaction().getSignature();

        // If block has signature
        // (blocks in UserConsentResponse doesn't, because signature is generated on server-side after the SerializableBlock is mapped into a Block)
        if ((base64EncodedStringSignature != null) && (!base64EncodedStringSignature.isEmpty())) {
            transaction.setSignature(stringUtil.base64DecodeString(base64EncodedStringSignature));
        } else {
            transaction.setSignature("".getBytes(StandardCharsets.UTF_8));
        }

        transaction.setTransactionId(stringUtil.base64DecodeString(serializableBlock.getTransaction().getTransactionId()));
        transaction.setRecord(serializableBlock.getTransaction().getRecord());

        String stringRecipientAddress = serializableBlock.getTransaction().getRecipientAddress();
        Address recipientAddress = new Address();
        recipientAddress.setAddress(stringRecipientAddress);
        transaction.setRecipientAddress(recipientAddress);

        String stringSenderAddress = serializableBlock.getTransaction().getSenderAddress();
        Address senderAddress = new Address();
        senderAddress.setAddress(stringSenderAddress);
        transaction.setSenderAddress(senderAddress);

        block.setTransaction(transaction);

        return block;
    }


    public Block mapAdditionRequestToBlock(BlockAddition blockAdditionRequest)
    {
        Block block = new Block();

        String networkUUID = blockAdditionRequest.getNetworkUUID();

        // Get a random provider in the network to set as a recipient
        User networkRandUser = networkService.getNetworkRandomMember(networkUUID);

        // Get the address of the provider
        String randUserProviderAddress = providerService.getProviderAddress(networkRandUser.getId());

        Address senderAddress = new Address(blockAdditionRequest.getSenderAddress());
        Address recipientAddress = new Address(randUserProviderAddress);

        // If there is no recipient, in case there is only one member in a network
        if (recipientAddress.getAddress().isEmpty()) {
            // Set the recipient as the sender
            transaction.setRecipientAddress(senderAddress);
        }
        else {
            transaction.setRecipientAddress(recipientAddress);
        }

        transaction.setSenderAddress(senderAddress);

        transaction.setRecord(new MedicalRecord());

        // Signature is added when patient gives consent for block addition (sharing their EHR)
        transaction.setSignature("".getBytes(StandardCharsets.UTF_8));

        transaction.setTransactionId(hashUtil.hashTransactionData(transaction));

        blockHeader.setIndex(Long.parseLong(blockAdditionRequest.getPreviousBlockIndex()) + 1);
        blockHeader.setPreviousHash(stringUtil.base64DecodeString(blockAdditionRequest.getPreviousBlockHash()));

        blockHeader.setMerkleLeafHash(hashUtil.SHA256(transaction.getTransactionId()));
        blockHeader.setNetworkUUID(blockAdditionRequest.getNetworkUUID());
        blockHeader.setTimeStamp(new Date().getTime());
        blockHeader.generateHeaderHash();

        block.setTransaction(transaction);
        block.setBlockHeader(blockHeader);

        return block;
    }


    public ConsentRequestBlock mapToConsentRequestBlock(Long userID, String providerUUID, String networkUUID, SerializableBlock block)
    {
        ConsentRequestBlock consentRequest = new ConsentRequestBlock();

        consentRequest.setUserID(userID);
        consentRequest.setRequestUUID(uuidUtil.generateUUID());
        consentRequest.setProviderUUID(providerUUID);
        consentRequest.setNetworkUUID(networkUUID);
        consentRequest.setRecipientAddress(block.getTransaction().getRecipientAddress());
        consentRequest.setSenderAddress(block.getTransaction().getSenderAddress());
        consentRequest.setTransactionId(block.getTransaction().getTransactionId());
        consentRequest.setMerkleLeafHash(block.getBlockHeader().getMerkleLeafHash());
        consentRequest.setBlockIndex(block.getBlockHeader().getIndex());
        consentRequest.setTimeStamp(block.getBlockHeader().getTimeStamp());
        consentRequest.setPreviousHash(block.getBlockHeader().getPreviousHash());
        consentRequest.setHash(block.getBlockHeader().getHash());

        return consentRequest;
    }


    private NetworkResponse mapNetworkToNetworkPayload(Network network)
    {
        if (network == null) {
            throw new NullUserNetworkException("Invalid Network");
        }

        if (network.getChainRoot() == null) {
            throw new NullUserNetworkException("Invalid network chain root");
        }

        String networkUUID = network.getNetworkUUID();
        String networkChainRoot = network.getChainRoot().getRoot();
        String networkName = network.getName();

        return new NetworkResponse(networkName, networkUUID, networkChainRoot);
    }


    public UserNetworksResponse mapNetworksToUserNetworksResponse(Set<Network> networks)
    {
        UserNetworksResponse userNetworksResponse = new UserNetworksResponse();

        Set<NetworkResponse> networkResponseSet = networks.stream().map(
                this::mapNetworkToNetworkPayload
        ).collect(Collectors.toSet());

        userNetworksResponse.setUserNetworks(networkResponseSet);

        return userNetworksResponse;
    }


    public NetworkInvitationRequest mapInvitationResponseToRequest(NetworkInvitationRequestPayload invitationResponse)
    {
        // Create a request object using the response fields (will be used to validate the
        // response by checking whether a request with the fields of the response exists or not)
        NetworkInvitationRequest invitationRequest = new NetworkInvitationRequest(
            invitationResponse.getSenderAddress(),
            invitationResponse.getNetworkName(),
            invitationResponse.getNetworkUUID(),
            invitationResponse.getInvitationToken()
        );

        return invitationRequest;
    }


    public NotificationResponse mapNotificationToNotificationResponse(Notification notification, Object reference)
    {
        return new NotificationResponse(
            notification.getId(),
            notification.getSender().getAddress(),
            notification.getRecipient().getAddress(),
            notification.getType().toString(),
            reference
        );
    }


    public NetworkInvitationRequestPayload mapNetworkInvitationRequestToPayload(NetworkInvitationRequest invitationRequest, String recipientAddress)
    {
        return new NetworkInvitationRequestPayload(
            recipientAddress,
            invitationRequest.getSenderAddress(),
            invitationRequest.getNetworkName(),
            invitationRequest.getNetworkUUID(),
            invitationRequest.getInvitationToken()
        );
    }


    public UserConsentRequest mapConsentRequestBlockToUserConsentRequest(ConsentRequestBlock consentRequestBlock)
    {
        MedicalRecord medicalRecord = new MedicalRecord();

        // Patients information are not stored on server-side, and are obtained from client-side
        // when the user accepts the consent request as a consent response.
        medicalRecord.setPatientInfo(new PatientInfo());

        // Record data is added after getting user consent
        medicalRecord.setProblems(new ArrayList<>());
        medicalRecord.setHistory(new HashMap<>());
        medicalRecord.setAllergiesAndReactions(new ArrayList<>());

        SerializableTransaction transaction = new SerializableTransaction(
                consentRequestBlock.getTransactionId(),
                medicalRecord,
                consentRequestBlock.getSenderAddress(),
                consentRequestBlock.getRecipientAddress(),
                consentRequestBlock.getSignature()
        );

        SerializableBlockHeader blockHeader = new SerializableBlockHeader(
                consentRequestBlock.getHash(),
                consentRequestBlock.getPreviousHash(),
                consentRequestBlock.getTimeStamp(),
                consentRequestBlock.getBlockIndex(),
                consentRequestBlock.getMerkleLeafHash(),
                consentRequestBlock.getNetworkUUID()
        );

        SerializableBlock block = new SerializableBlock(blockHeader, transaction);

        return new UserConsentRequest(
                consentRequestBlock.getRequestUUID(),
                block,
                consentRequestBlock.getProviderUUID(),
                consentRequestBlock.getNetworkUUID(),
                consentRequestBlock.getUserID()
        );
    }


    public MedicalRecord mapEhrDetailsToMedicalRecord(EhrDetails ehrDetails, PatientInfo patientInfo)
    {
        List<String> medicalProblems = ehrDetails.getProblems().stream().map(
            EhrProblems::getProblem
        ).collect(Collectors.toList());

        List<String> allergies = ehrDetails.getAllergies().stream().map(
            EhrAllergies::getAllergy
        ).collect(Collectors.toList());

        Map<String, Boolean> medicalHistory = ehrDetails.getHistory().stream().collect(
            Collectors.toMap(EhrHistory::getCondition, EhrHistory::isOccurrence)
        );

        return new MedicalRecord(patientInfo, medicalProblems, allergies, medicalHistory);
    }
}
