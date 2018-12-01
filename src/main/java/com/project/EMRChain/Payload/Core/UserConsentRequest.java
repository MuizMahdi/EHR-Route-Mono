package com.project.EMRChain.Payload.Core;

public class UserConsentRequest
{
    private SerializableBlock block;
    private String chainRootWithBlock;
    private String providerUUID;
    private String userID;

    public UserConsentRequest(SerializableBlock block, String chainRootWithBlock, String providerUUID, String userID) {
        this.block = block;
        this.chainRootWithBlock = chainRootWithBlock;
        this.providerUUID = providerUUID;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
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

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public void setBlock(SerializableBlock block) {
        this.block = block;
    }
    public void setProviderUUID(String providerUUID) {
        this.providerUUID = providerUUID;
    }
    public void setChainRootWithBlock(String chainRootWithBlock) {
        this.chainRootWithBlock = chainRootWithBlock;
    }
}
