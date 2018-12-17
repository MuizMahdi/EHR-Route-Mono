package com.project.EhrRoute.Utilities;
import com.project.EhrRoute.Core.*;
import com.project.EhrRoute.Core.Utilities.KeyUtil;
import com.project.EhrRoute.Core.Utilities.StringUtil;
import com.project.EhrRoute.Entities.Core.ConsentRequestBlock;
import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Exceptions.NullUserNetworkException;
import com.project.EhrRoute.Payload.Core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class ModelMapper
{
    private StringUtil stringUtil;
    private KeyUtil keyUtil;

    @Autowired
    public ModelMapper(StringUtil stringUtil, KeyUtil keyUtil) {
        this.stringUtil = stringUtil;
        this.keyUtil = keyUtil;
    }

    public SerializableBlock mapBlockToSerializableBlock(Block block) throws GeneralSecurityException
    {
        /*                                                            */
        // ||||| Populate the SerializableBlock with Block data ||||| //
        /*                                                            */

        SerializableBlock serializableBlock = new SerializableBlock();

        SerializableBlockHeader serializableBlockHeader = new SerializableBlockHeader();
        SerializableTransaction serializableTransaction = new SerializableTransaction();

        serializableBlockHeader.setHash(stringUtil.getStringFromBytes(block.getBlockHeader().getHash()));
        serializableBlockHeader.setPreviousHash(stringUtil.getStringFromBytes(block.getBlockHeader().getPreviousHash()));
        serializableBlockHeader.setTimeStamp(block.getBlockHeader().getTimeStamp());
        serializableBlockHeader.setIndex(block.getBlockHeader().getIndex());
        serializableBlockHeader.setMerkleRoot(stringUtil.getStringFromBytes(block.getBlockHeader().getMerkleRoot()));
        serializableBlockHeader.setNetworkUUID(block.getBlockHeader().getNetworkUUID());

        serializableBlock.setBlockHeader(serializableBlockHeader);

        byte[] blockTxID = block.getTransaction().getTransactionId();
        serializableTransaction.setTransactionId(stringUtil.getStringFromBytes(blockTxID));
        serializableTransaction.setRecord(block.getTransaction().getRecord());

        PublicKey publicKey = block.getTransaction().getSenderPubKey();
        String stringPublicKey = keyUtil.getStringFromPublicKey(publicKey);
        serializableTransaction.setSenderPubKey(stringPublicKey);

        serializableTransaction.setSenderAddress(block.getTransaction().getSenderAddress().getAddress());
        serializableTransaction.setRecipientAddress(block.getTransaction().getRecipientAddress().getAddress());

        byte[] signatureBytes = block.getTransaction().getSignature();
        serializableTransaction.setSignature(stringUtil.base64EncodeBytes(signatureBytes));

        serializableBlock.setTransaction(serializableTransaction);

        return serializableBlock;
    }

    public Block mapSerializableBlockToBlock(SerializableBlock serializableBlock) throws GeneralSecurityException
    {
        Block block = new Block();

        BlockHeader blockHeader = new BlockHeader();

        blockHeader.setIndex(serializableBlock.getBlockHeader().getIndex());
        blockHeader.setTimeStamp(serializableBlock.getBlockHeader().getTimeStamp());

        String stringHash = serializableBlock.getBlockHeader().getHash();
        blockHeader.setHash(stringHash.getBytes());

        String stringPreviousHash = serializableBlock.getBlockHeader().getPreviousHash();
        blockHeader.setPreviousHash(stringPreviousHash.getBytes());

        String stringMerkleRoot = serializableBlock.getBlockHeader().getMerkleRoot();
        blockHeader.setMerkleRoot(stringMerkleRoot.getBytes());

        String networkUUID = serializableBlock.getBlockHeader().getNetworkUUID();
        blockHeader.setNetworkUUID(networkUUID);

        block.setBlockHeader(blockHeader);

        Transaction transaction = new Transaction();

        String base64EncodedStringSignature = serializableBlock.getTransaction().getSignature();
        transaction.setSignature(stringUtil.base64DecodeString(base64EncodedStringSignature));


        transaction.setTransactionId(serializableBlock.getTransaction().getTransactionId().getBytes());
        transaction.setRecord(serializableBlock.getTransaction().getRecord());

        String stringRecipientAddress = serializableBlock.getTransaction().getRecipientAddress();
        Address recipientAddress = new Address();
        recipientAddress.setAddress(stringRecipientAddress);
        transaction.setRecipientAddress(recipientAddress);

        String stringSenderAddress = serializableBlock.getTransaction().getSenderAddress();
        Address senderAddress = new Address();
        senderAddress.setAddress(stringSenderAddress);
        transaction.setSenderAddress(senderAddress);

        String stringSenderPubKey = serializableBlock.getTransaction().getSenderPubKey();
        PublicKey senderPubKey = keyUtil.getPublicKeyFromString(stringSenderPubKey);
        transaction.setSenderPubKey(senderPubKey);

        block.setTransaction(transaction);

        return block;
    }

    public ConsentRequestBlock mapToConsentRequestBlock(Long userID, String providerUUID, SerializableBlock block, String chainRootWithBlock)
    {
        ConsentRequestBlock consentRequest = new ConsentRequestBlock();

        consentRequest.setUserID(userID);
        consentRequest.setProviderUUID(providerUUID);
        consentRequest.setChainRootWithBlock(chainRootWithBlock);
        consentRequest.setRecipientAddress(block.getTransaction().getRecipientAddress());
        consentRequest.setSenderAddress(block.getTransaction().getSenderAddress());
        consentRequest.setSenderPubKey(block.getTransaction().getSenderPubKey());
        consentRequest.setTransactionId(block.getTransaction().getTransactionId());
        consentRequest.setMerkleRoot(block.getBlockHeader().getMerkleRoot());
        consentRequest.setBlockIndex(block.getBlockHeader().getIndex());
        consentRequest.setTimeStamp(block.getBlockHeader().getTimeStamp());
        consentRequest.setNetworkUUID(block.getBlockHeader().getNetworkUUID());
        consentRequest.setPreviousHash(block.getBlockHeader().getPreviousHash());
        consentRequest.setHash(block.getBlockHeader().getHash());

        return consentRequest;
    }

    private NetworkResponse mapNetworkToNetworkPayload(Network network) throws NullUserNetworkException
    {
        if (network == null) {
            throw new NullUserNetworkException("Invalid Network");
        }

        if (network.getChainRoot() == null) {
            throw new NullUserNetworkException("Invalid network chain root");
        }

        String networkUUID = network.getNetworkUUID();
        String networkChainRoot = network.getChainRoot().getRoot();

        return new NetworkResponse(networkUUID, networkChainRoot);
    }

    public UserNetworksResponse mapNetworksToUserNetworksResponse(Set<Network> networks) throws NullUserNetworkException
    {
        UserNetworksResponse userNetworksResponse = new UserNetworksResponse();

        Set<NetworkResponse> networkResponseSet = networks.stream().map(
                this::mapNetworkToNetworkPayload
        ).collect(Collectors.toSet());

        userNetworksResponse.setUserNetworks(networkResponseSet);

        return userNetworksResponse;
    }
}
