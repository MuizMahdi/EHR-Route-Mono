package com.project.EhrRoute.Entities.App;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "network_invitation_requests")
public class NetworkInvitationRequest
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Invitation sender name
    @NotNull
    @NotBlank
    private String senderName;

    // Network that has been invited to join
    @NotNull
    @NotBlank
    private String networkName;

    // The network UUID;
    @NotNull
    @NotBlank
    private String networkUUID;

    // The token of a verification token, which lasts for 24 hours, and is used to accept the invitation.
    @NotNull
    @NotBlank
    private String invitationToken;


    public NetworkInvitationRequest() { }
    public NetworkInvitationRequest(@NotNull @NotBlank String senderName, @NotNull @NotBlank String networkName, @NotNull @NotBlank String networkUUID) {
        this.senderName = senderName;
        this.networkName = networkName;
        this.networkUUID = networkUUID;
    }


    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
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
}
