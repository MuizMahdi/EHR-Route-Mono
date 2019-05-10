package com.project.EhrRoute.Payload.Core;

public class UserConsentRequest
{
    private SerializableBlock block;
    private String providerUUID;
    private String networkUUID;
    private Long userID;

    public UserConsentRequest(SerializableBlock block,  String providerUUID, String networkUUID, Long userID) {
        this.block = block;
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
    public String getProviderUUID() {
        return providerUUID;
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
    public void setProviderUUID(String providerUUID) {
        this.providerUUID = providerUUID;
    }
}
