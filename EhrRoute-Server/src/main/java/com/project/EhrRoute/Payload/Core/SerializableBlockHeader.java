package com.project.EhrRoute.Payload.Core;

public class SerializableBlockHeader
{
    private String hash;
    private String previousHash;
    private long timeStamp;
    private long index;
    private String merkleLeafHash;
    private String networkUUID;

    public SerializableBlockHeader() { }
    public SerializableBlockHeader(String hash, String previousHash, long timeStamp, long index, String merkleLeafHash, String networkUUID) {
        this.hash = hash;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.index = index;
        this.merkleLeafHash = merkleLeafHash;
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
    public String getNetworkUUID() {
        return networkUUID;
    }
    public String getPreviousHash() {
        return previousHash;
    }
    public String getMerkleLeafHash() {
        return merkleLeafHash;
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
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }
    public void setMerkleLeafHash(String merkleLeafHash) {
        this.merkleLeafHash = merkleLeafHash;
    }

    @Override
    public String toString() {
        return "SerializableBlockHeader{" +
                "hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", timeStamp=" + timeStamp +
                ", index=" + index +
                ", merkleLeafHash='" + merkleLeafHash + '\'' +
                ", networkUUID='" + networkUUID + '\'' +
                '}';
    }
}
