package com.project.EhrRoute.Payload.Core;

public class SerializableBlockHeader
{
    private String hash;
    private String previousHash;
    private long timeStamp;
    private long index;
    private String merkleRoot;
    private String networkUUID;

    public SerializableBlockHeader() { }
    public SerializableBlockHeader(String hash, String previousHash, long timeStamp, long index, String merkleRoot, String networkUUID) {
        this.hash = hash;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.index = index;
        this.merkleRoot = merkleRoot;
        this.networkUUID = networkUUID;
    }

    public long getIndex() {
        return index;
    }
    public String getHash() {
        return hash;
    }
    public long getTimeStamp() {
        return timeStamp;
    }
    public String getMerkleRoot() {
        return merkleRoot;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public String getPreviousHash() {
        return previousHash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
    public void setIndex(long index) {
        this.index = index;
    }
    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }
}
