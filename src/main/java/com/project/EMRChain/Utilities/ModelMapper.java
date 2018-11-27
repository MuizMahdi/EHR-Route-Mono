package com.project.EMRChain.Utilities;
import com.project.EMRChain.Core.*;
import com.project.EMRChain.Core.Utilities.StringUtil;
import com.project.EMRChain.Entities.Core.ConsentRequestBlock;
import com.project.EMRChain.Payload.Core.SerializableBlock;
import com.project.EMRChain.Payload.Core.SerializableBlockHeader;
import com.project.EMRChain.Payload.Core.SerializableTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper
{
    private StringUtil stringUtil;

    @Autowired
    public ModelMapper(StringUtil stringUtil) {
        this.stringUtil = stringUtil;
    }
    public ModelMapper() { }

    public SerializableBlock mapBlockToSerializableBlock(Block block)
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
        serializableTransaction.setSenderPubKey(stringUtil.getStringFromKey(block.getTransaction().getSenderPubKey()));
        serializableTransaction.setSenderAddress(block.getTransaction().getSenderAddress().getAddress());
        serializableTransaction.setRecipientAddress(block.getTransaction().getRecipientAddress().getAddress());
        serializableTransaction.setSignature(stringUtil.getStringFromBytes(block.getTransaction().getSignature()));

        serializableBlock.setTransaction(serializableTransaction);

        return serializableBlock;
    }
}
