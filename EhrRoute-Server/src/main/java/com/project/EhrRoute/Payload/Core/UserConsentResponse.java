package com.project.EhrRoute.Payload.Core;


public class UserConsentResponse
{
    private SerializableBlock block;
    private String chainRootWithBlock;
    private String userPrivateKey;
    private String providerUUID;
    private String userID;

    public UserConsentResponse() { }
    public UserConsentResponse(SerializableBlock block, String chainRootWithBlock, String userPrivateKey, String providerUUID, String userID) {
        this.block = block;
        this.chainRootWithBlock = chainRootWithBlock;
        this.userPrivateKey = userPrivateKey;
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
    public String getUserPrivateKey() {
        return userPrivateKey;
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
    public void setUserPrivateKey(String userPrivateKey) {
        this.userPrivateKey = userPrivateKey;
    }
    public void setChainRootWithBlock(String chainRootWithBlock) {
        this.chainRootWithBlock = chainRootWithBlock;
    }
}
