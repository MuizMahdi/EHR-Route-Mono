package com.project.EhrRoute.Payload.Core;

public class BlockAddition
{
    private String chainRootWithoutBlock;
    private String previousBlockIndex;
    private String previousBlockHash;
    private String senderAddress;
    private String providerUUID;
    private String networkUUID;
    private String ehrUserID;


    public BlockAddition() { }
    public BlockAddition(String chainRootWithoutBlock, String previousBlockIndex, String previousBlockHash, String senderAddress, String providerUUID, String networkUUI, String ehrUserID) {
        this.chainRootWithoutBlock = chainRootWithoutBlock;
        this.previousBlockIndex = previousBlockIndex;
        this.previousBlockHash = previousBlockHash;
        this.senderAddress = senderAddress;
        this.providerUUID = providerUUID;
        this.networkUUID = networkUUI;
        this.ehrUserID = ehrUserID;
    }


    public String getEhrUserID() {
        return ehrUserID;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public String getProviderUUID() {
        return providerUUID;
    }
    public String getSenderAddress() {
        return senderAddress;
    }
    public String getPreviousBlockHash() {
        return previousBlockHash;
    }
    public String getPreviousBlockIndex() {
        return previousBlockIndex;
    }
    public String getChainRootWithoutBlock() {
        return chainRootWithoutBlock;
    }

    public void setChainRootWithoutBlock(String chainRootWithoutBlock) {
        this.chainRootWithoutBlock = chainRootWithoutBlock;
    }
    public void setEhrUserID(String ehrUserID) {
        this.ehrUserID = ehrUserID;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
    public void setProviderUUID(String providerUUID) {
        this.providerUUID = providerUUID;
    }
    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }
    public void setPreviousBlockHash(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }
    public void setPreviousBlockIndex(String previousBlockIndex) {
        this.previousBlockIndex = previousBlockIndex;
    }
}
