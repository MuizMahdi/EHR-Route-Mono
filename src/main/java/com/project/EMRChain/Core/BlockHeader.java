package com.project.EMRChain.Core;

public class BlockHeader
{
    private BlockHash hash; // Hash of the block
    private BlockHash previousHash; // Hash of previous block
    private long timeStamp; // Block timestamp
    private long index; // Index number of the block in the chain

    // The hash containing the transaction in this block, used for calculating merkleRoot of
    // the whole chain tree to validate the whole chain.
    private MerkleRoot merkleRoot;


    public BlockHeader() { }
    public BlockHeader(BlockHash hash, BlockHash previousHash, MerkleRoot merkleRoot) {
        this.hash = hash;
        this.previousHash = previousHash;
        this.merkleRoot = merkleRoot;
    }
    public BlockHeader(BlockHash hash, BlockHash previousHash, long timeStamp, long index, MerkleRoot merkleRoot) {
        this.hash = hash;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.index = index;
        this.merkleRoot = merkleRoot;
    }


    public long getIndex() {
        return index;
    }
    public BlockHash getHash() {
        return hash;
    }
    public long getTimeStamp() {
        return timeStamp;
    }
    public MerkleRoot getMerkleRoot() {
        return merkleRoot;
    }
    public BlockHash getPreviousHash() {
        return previousHash;
    }

    public void setIndex(long index) {
        this.index = index;
    }
    public void setHash(BlockHash hash) {
        this.hash = hash;
    }
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
    public void setMerkleRoot(MerkleRoot merkleRoot) {
        this.merkleRoot = merkleRoot;
    }
    public void setPreviousHash(BlockHash previousHash) {
        this.previousHash = previousHash;
    }
}
