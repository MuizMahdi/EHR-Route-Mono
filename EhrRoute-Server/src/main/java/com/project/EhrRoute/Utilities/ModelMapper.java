package com.project.EhrRoute.Utilities;
import com.project.EhrRoute.Core.*;
import com.project.EhrRoute.Core.Utilities.KeyUtil;
import com.project.EhrRoute.Core.Utilities.StringUtil;
import com.project.EhrRoute.Entities.Core.ConsentRequestBlock;

import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Exceptions.NullUserNetworkException;
import com.project.EhrRoute.Exceptions.ResourceEmptyException;
import com.project.EhrRoute.Payload.Core.NetworkResponse;
import com.project.EhrRoute.Payload.Core.SerializableBlock;
import com.project.EhrRoute.Payload.Core.SerializableBlockHeader;
import com.project.EhrRoute.Payload.Core.SerializableTransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;
import java.security.PublicKey;


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

        serializableBlockHeader.setHash(block.getBlockHeader().getHash().getString());
        serializableBlockHeader.setPreviousHash(block.getBlockHeader().getPreviousHash().getString());
        serializableBlockHeader.setTimeStamp(block.getBlockHeader().getTimeStamp());
        serializableBlockHeader.setIndex(block.getBlockHeader().getIndex());
        serializableBlockHeader.setMerkleRoot(stringUtil.getStringFromBytes(block.getBlockHeader().getMerkleRoot()));

        serializableBlock.setBlockHeader(serializableBlockHeader);

        serializableTransaction.setTransactionId(block.getTransaction().getTransactionId());
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
        Hash hash = new Hash(stringHash.getBytes());
        blockHeader.setHash(hash);

        String stringPreviousHash = serializableBlock.getBlockHeader().getPreviousHash();
        Hash previousHash = new Hash(stringPreviousHash.getBytes());
        blockHeader.setPreviousHash(previousHash);

        String stringMerkleRoot = serializableBlock.getBlockHeader().getMerkleRoot();
        blockHeader.setMerkleRoot(stringMerkleRoot.getBytes());

        block.setBlockHeader(blockHeader);

        Transaction transaction = new Transaction();

        String base64EncodedStringSignature = serializableBlock.getTransaction().getSignature();
        transaction.setSignature(stringUtil.base64DecodeString(base64EncodedStringSignature));

        transaction.setTransactionId(serializableBlock.getTransaction().getTransactionId());
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
        consentRequest.setPreviousHash(block.getBlockHeader().getPreviousHash());
        consentRequest.setHash(block.getBlockHeader().getHash());

        return consentRequest;
    }

    public NetworkResponse mapNetworkToNetworkPayload(Network network)
    {
        if (network == null) {
            throw new ResourceEmptyException("Invalid Network");
        }

        if (network.getChainRoot() == null) {
            throw new ResourceEmptyException("Invalid network chain root");
        }

        String networkUUID = network.getNetworkUUID();
        String networkChainRoot = network.getChainRoot().getRoot();

        return new NetworkResponse(networkUUID, networkChainRoot);
    }
}
