package com.project.EMRChain.Payload.Core;

public class BlockAddition
{
    private SerializableBlock block;
    private String chainRootWithoutBlock;
    private String chainRootWithBlock;
    private String providerUUID;
    private String ehrUserID;

    public BlockAddition() { }
    public BlockAddition(SerializableBlock block, String chainRootWithoutBlock, String chainRootWithBlock, String providerUUID, String ehrUserID) {
        this.block = block;
        this.chainRootWithoutBlock = chainRootWithoutBlock;
        this.chainRootWithBlock = chainRootWithBlock;
        this.providerUUID = providerUUID;
        this.ehrUserID = ehrUserID;
    }

    public String getEhrUserID() {
        return ehrUserID;
    }
    public SerializableBlock getBlock() {
        return block;
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
    public void setProviderUUID(String providerUUID) {
        this.providerUUID = providerUUID;
    }
    public void setChainRootWithBlock(String chainRootWithBlock) {
        this.chainRootWithBlock = chainRootWithBlock;
    }
}
