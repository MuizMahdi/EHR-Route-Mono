package com.project.EhrRoute.Payload.Core;

public class BlockAddition
{
    private String chainRootWithoutBlock;
    private String providerUUID;
    private String networkUUID;
    private String ehrUserID;

    public BlockAddition() { }
    public BlockAddition(String chainRootWithoutBlock, String providerUUID, String networkUUI, String ehrUserID) {
        this.block = block;
        this.chainRootWithoutBlock = chainRootWithoutBlock;
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
}
