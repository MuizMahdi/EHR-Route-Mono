package com.project.EhrRoute.Payload.Core;

public class BlockAddition
{
    private SerializableBlock block;
    private String chainRootWithoutBlock;
    private String chainRootWithBlock;
    private String providerUUID;
    private String networkUUID;
    private String ehrUserID;

    public BlockAddition() { }
    public BlockAddition(SerializableBlock block, String chainRootWithoutBlock, String chainRootWithBlock, String providerUUID, String networkUUI, String ehrUserID) {
        this.block = block;
        this.chainRootWithoutBlock = chainRootWithoutBlock;
        this.chainRootWithBlock = chainRootWithBlock;
        this.providerUUID = providerUUID;
        this.networkUUID = networkUUI;
        this.ehrUserID = ehrUserID;
    }

    public String getEhrUserID() {
        return ehrUserID;
    }
    public SerializableBlock getBlock() {
        return block;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public String getProviderUUID() {
        return providerUUID;
    }
    public String getChainRootWithBlock() {
        return chainRootWithBlock;
    }
    public String getChainRootWithoutBlock() {
        return chainRootWithoutBlock;
    }

    public void setBlock(SerializableBlock block) {
        this.block = block;
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
    public void setChainRootWithBlock(String chainRootWithBlock) {
        this.chainRootWithBlock = chainRootWithBlock;
    }
}
