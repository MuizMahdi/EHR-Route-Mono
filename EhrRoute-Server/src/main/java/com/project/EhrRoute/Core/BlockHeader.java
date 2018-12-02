package com.project.EMRChain.Core;

import java.util.Date;

public class BlockHeader
{
    private Hash hash; // Hash of the block
    private Hash previousHash; // Hash of previous block
    private long timeStamp; // Block timestamp
    private long index; // Index number of the block in the chain

    // Hash of transaction in this block, used for calculating merkleRoot of
    // the whole Blockchain tree to validate the whole chain.
    private byte[] merkleRoot;

    public BlockHeader() {
        this.timeStamp = new Date().getTime();
    }
    public BlockHeader(Hash hash, Hash previousHash, byte[] merkleRoot) {
        this.hash = hash;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.merkleRoot = merkleRoot;
    }
    public BlockHeader(Hash hash, Hash previousHash, long index, byte[] merkleRoot) {
        this.hash = hash;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.index = index;
        this.merkleRoot = merkleRoot;
    }


    public long getIndex() {
        return index;
    }
    public Hash getHash() {
        return hash;
    }
    public long getTimeStamp() {
        return timeStamp;
    }
    public Hash getPreviousHash() {
        return previousHash;
    }
    public byte[] getMerkleRoot() {
        return merkleRoot;
    }

    public void setHash(Hash hash) {
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
    public void setPreviousHash(Hash previousHash) {
        this.previousHash = previousHash;
    }
}
