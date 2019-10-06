package com.project.EhrRoute.Payload.App;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class NetworkInvitationRequestPayload
{
    @NotNull @NotBlank private String recipientAddress;
    @NotNull @NotBlank private String senderAddress;
    @NotNull @NotBlank private String networkName;
    @NotNull @NotBlank private String networkUUID;
    @NotNull @NotBlank private String invitationToken;


    public NetworkInvitationRequestPayload() { }
    public NetworkInvitationRequestPayload(String recipientAddress, String senderAddress, String networkName, String networkUUID, String invitationToken) {
        this.recipientAddress = recipientAddress;
        this.senderAddress = senderAddress;
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
    public String getSenderAddress() {
        return senderAddress;
    }
    public String getInvitationToken() {
        return invitationToken;
    }
    public String getRecipientAddress() {
        return recipientAddress;
    }
    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }
    public void setInvitationToken(String invitationToken) {
        this.invitationToken = invitationToken;
    }
    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }
}
