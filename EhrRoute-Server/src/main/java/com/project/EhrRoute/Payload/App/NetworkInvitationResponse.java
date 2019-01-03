package com.project.EhrRoute.Payload.App;

public class NetworkInvitationResponse
{
    private String recipientName;
    private String networkName;
    private String networkUUID;
    private String senderName;
    private String token;


    public NetworkInvitationResponse() { }
    public NetworkInvitationResponse(String recipientName, String networkName, String networkUUID, String senderName, String token) {
        this.recipientName = recipientName;
        this.networkName = networkName;
        this.networkUUID = networkUUID;
        this.senderName = senderName;
        this.token = token;
    }


    public String getToken() {
        return token;
    }
    public String getSenderName() {
        return senderName;
    }
    public String getNetworkName() {
        return networkName;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public String getRecipientName() {
        return recipientName;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
}
