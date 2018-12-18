package com.project.EhrRoute.Core;
import com.project.EhrRoute.Core.Utilities.HashUtil;
import com.project.EhrRoute.Core.Utilities.RsaUtil;
import com.project.EhrRoute.Entities.EHR.MedicalRecord;
import com.project.EhrRoute.Entities.EHR.PatientInfo;
import com.project.EhrRoute.Utilities.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


@Component
public class GenesisBlock
{
    private HashUtil hashUtil;
    private RsaUtil rsaUtil;
    private UuidUtil uuidUtil;

    // Genesis block contains a Block so that it could be mapped directly to SerializableBlock in the ModelMapper
    private Block block;
    private Transaction transaction;
    private BlockHeader blockHeader;

    @Autowired
    public GenesisBlock(HashUtil hashUtil, RsaUtil rsaUtil, UuidUtil uuidUtil, Transaction transaction, BlockHeader blockHeader) throws Exception {
        this.hashUtil = hashUtil;
        this.rsaUtil = rsaUtil;
        this.uuidUtil = uuidUtil;
        this.transaction = transaction;
        this.blockHeader = blockHeader;
    }


    public void initializeBlock() throws Exception
    {
        // Initialize genesis block data

        Block block = new Block();

        Address senderAddress = new Address();
        senderAddress.setAddress("0A0");
        Address recipientAddress = new Address();
        recipientAddress.setAddress("0B0");

        // Empty record
        MedicalRecord record = new MedicalRecord(
                new PatientInfo("", "", "", 0, "", "", "", "", 0),
                new ArrayList<>(),
                new ArrayList<>(),
                new HashMap<>()
        );

        KeyPair keyPair = rsaUtil.rsaGenerateKeyPair();


        transaction.setRecipientAddress(recipientAddress);
        transaction.setSenderPubKey(keyPair.getPublic());
        transaction.setSenderAddress(senderAddress);
        transaction.setRecord(record);
        transaction.setTransactionId(hashUtil.hashTransactionData(transaction));
        transaction.setSignature(rsaUtil.rsaSign(keyPair.getPrivate(), transaction));

        blockHeader.setIndex(0);
        blockHeader.setTimeStamp(new Date().getTime());
        blockHeader.setNetworkUUID(uuidUtil.generateUUID());
        blockHeader.setMerkleRoot(hashUtil.hashTransactionData(transaction));
        blockHeader.setPreviousHash(hashUtil.SHA256("0".getBytes()));
        blockHeader.generateHeaderHash();

        block.setTransaction(transaction);
        block.setBlockHeader(blockHeader);

        this.block = block;
    }


    public Block getBlock() {
        return block;
    }
    public void setBlock(Block block) {
        this.block = block;
    }

}
