package com.project.EhrRoute.Core;
import com.project.EhrRoute.Core.Utilities.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class BlockHeader
{
    private HashUtil hashUtil;

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
    private byte[] merkleRoot;

    @Autowired
    public BlockHeader(HashUtil hashUtil) {
        this.hashUtil = hashUtil;
    }
    public BlockHeader() {
        this.timeStamp = new Date().getTime();
    }
    public BlockHeader(byte[] hash, byte[] previousHash, byte[] merkleRoot) {
        this.hash = hash;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.merkleRoot = merkleRoot;
    }
    public BlockHeader(byte[] hash, byte[] previousHash, long index, byte[] merkleRoot) {
        this.hash = hash;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.index = index;
        this.merkleRoot = merkleRoot;
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
    public byte[] getMerkleRoot() {
        return merkleRoot;
    }
    public byte[] getPreviousHash() {
        return previousHash;
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
    public void setMerkleRoot(byte[] merkleRoot) {
        this.merkleRoot = merkleRoot;
    }
    public void setPreviousHash(byte[] previousHash) {
        this.previousHash = previousHash;
    }
}
