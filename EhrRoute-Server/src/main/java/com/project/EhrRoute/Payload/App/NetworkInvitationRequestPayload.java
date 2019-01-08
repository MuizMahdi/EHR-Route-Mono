package com.project.EhrRoute.Payload.App;


public class NetworkInvitationRequestPayload
{
    private String recipientUsername;
    private String senderUsername;
    private String networkName;
    private String networkUUID;


    public NetworkInvitationRequestPayload() { }
    public NetworkInvitationRequestPayload(String recipientUsername, String senderUsername, String networkName, String networkUUID) {
        this.recipientUsername = recipientUsername;
        this.senderUsername = senderUsername;
        this.networkName = networkName;
        this.networkUUID = networkUUID;
    }


    public String getNetworkName() {
        return networkName;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public String getSenderUsername() {
        return senderUsername;
    }
    public String getRecipientUsername() {
        return recipientUsername;
    }
    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }
    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }
}
