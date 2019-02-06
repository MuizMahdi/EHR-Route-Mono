package com.project.EhrRoute.Core;
import com.project.EhrRoute.Core.Utilities.HashUtil;
import com.project.EhrRoute.Core.Utilities.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class BlockHeader
{
    private HashUtil hashUtil;
    private StringUtil stringUtil;

    // Hash of the block header
    private byte[] hash;

    // Hash of previous block's header
    private byte[] previousHash;

    // Block timestamp
    private long timeStamp;

    // Index number of the block in the chain
    private long index;

    // Hash of transaction in this block, used for calculating merkleRoot of
    // the whole Blockchain tree to validate the whole chain.
    private byte[] merkleLeafHash;

    // NetworkUUID of the network in which the chain is in
    private String networkUUID;

    @Autowired
    public BlockHeader(HashUtil hashUtil, StringUtil stringUtil) {
        this.hashUtil = hashUtil;
        this.stringUtil = stringUtil;
    }


    public BlockHeader() {
        this.timeStamp = new Date().getTime();
    }
    public BlockHeader(long index, byte[] previousHash, byte[] merkleLeafHash) {
        this.index = index;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.merkleLeafHash = merkleLeafHash;
    }
    public BlockHeader(byte[] hash, byte[] previousHash, long index, byte[] merkleLeafHash) {
        this.hash = hash;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.index = index;
        this.merkleLeafHash = merkleLeafHash;
    }

    public void generateHeaderHash() {
        // Get Long objects in order to convert the values to string
        Long TimeStamp = timeStamp;
        Long Index = index;

        // Add up all of the data
        String stringHeaderData =
        stringUtil.getStringFromBytes(previousHash) +
        stringUtil.getStringFromBytes(merkleLeafHash) +
        TimeStamp.toString() + Index.toString() + networkUUID;

        // Block hash is the hash of the header data
        hash = hashUtil.SHA256(stringHeaderData.getBytes());
    }


    public byte[] getHash() {
        return hash;
    }
    public long getIndex() {
        return index;
    }
    public long getTimeStamp() {
        return timeStamp;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public byte[] getPreviousHash() {
        return previousHash;
    }
    public byte[] getMerkleLeafHash() {
        return merkleLeafHash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }
    public void setIndex(long index) {
        this.index = index;
    }
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
    public void setPreviousHash(byte[] previousHash) {
        this.previousHash = previousHash;
    }
    public void setMerkleLeafHash(byte[] merkleLeafHash) {
        this.merkleLeafHash = merkleLeafHash;
    }
}
