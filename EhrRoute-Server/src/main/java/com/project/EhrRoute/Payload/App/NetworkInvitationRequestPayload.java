package com.project.EhrRoute.Payload.App;


public class NetworkInvitationRequestPayload
{
    private String invitationRecipientUsernameOrEmail;
    private String senderName;
    private String networkName;
    private String networkUUID;


    public NetworkInvitationRequestPayload() { }
    public NetworkInvitationRequestPayload(String invitationRecipientUsernameOrEmail, String senderName, String networkName, String networkUUID) {
        this.invitationRecipientUsernameOrEmail = invitationRecipientUsernameOrEmail;
        this.senderName = senderName;
        this.networkName = networkName;
        this.networkUUID = networkUUID;
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
    public String getInvitationRecipientUsernameOrEmail() {
        return invitationRecipientUsernameOrEmail;
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
    public void setInvitationRecipientUsernameOrEmail(String invitationRecipientUsernameOrEmail) {
        this.invitationRecipientUsernameOrEmail = invitationRecipientUsernameOrEmail;
    }
}
