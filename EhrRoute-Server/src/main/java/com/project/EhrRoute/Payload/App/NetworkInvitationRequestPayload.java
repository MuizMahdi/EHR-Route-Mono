package com.project.EhrRoute.Payload.App;


public class NetworkInvitationRequestPayload
{
    private String recipientUsername;
    private String senderUsername;
    private String networkName;
    private String networkUUID;
    private String invitationToken;


    public NetworkInvitationRequestPayload() { }
    public NetworkInvitationRequestPayload(String recipientUsername, String senderUsername, String networkName, String networkUUID, String invitationToken) {
        this.recipientUsername = recipientUsername;
        this.senderUsername = senderUsername;
        this.networkName = networkName;
        this.networkUUID = networkUUID;
        this.invitationToken = invitationToken;
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
    public String getInvitationToken() {
        return invitationToken;
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
    public void setInvitationToken(String invitationToken) {
        this.invitationToken = invitationToken;
    }
    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }
}
