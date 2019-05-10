package com.project.EhrRoute.Payload.Core;


public class UserConsentResponse
{
    private SerializableBlock block;
    private String userPrivateKey;
    private String userAddress;
    private String providerUUID;
    private String networkUUID;
    private Long userID;


    public UserConsentResponse() { }
    public UserConsentResponse(SerializableBlock block, String userPrivateKey, String userAddress, String providerUUID, String networkUUID, Long userID) {
        this.block = block;
        this.userPrivateKey = userPrivateKey;
        this.userAddress = userAddress;
        this.providerUUID = providerUUID;
        this.networkUUID = networkUUID;
        this.userID = userID;
    }


    public Long getUserID() {
        return userID;
    }
    public SerializableBlock getBlock() {
        return block;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public String getUserAddress() {
        return userAddress;
    }
    public String getProviderUUID() {
        return providerUUID;
    }
    public String getUserPrivateKey() {
        return userPrivateKey;
    }
    public void setUserID(Long userID) {
        this.userID = userID;
    }
    public void setBlock(SerializableBlock block) {
        this.block = block;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
    public void setProviderUUID(String providerUUID) {
        this.providerUUID = providerUUID;
    }
    public void setUserPrivateKey(String userPrivateKey) {
        this.userPrivateKey = userPrivateKey;
    }
}
