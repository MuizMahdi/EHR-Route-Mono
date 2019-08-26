package com.project.EhrRoute.Utilities;
import com.project.EhrRoute.Entities.App.NetworkInvitationRequest;
import com.project.EhrRoute.Entities.App.Notification;
import com.project.EhrRoute.Entities.Core.ConsentRequestBlock;
import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Exceptions.InvalidNotificationException;
import com.project.EhrRoute.Exceptions.NullUserNetworkException;
import com.project.EhrRoute.Models.NotificationType;
import com.project.EhrRoute.Services.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class NotificationUtil
{
    private NetworkService networkService;

    @Autowired
    public NotificationUtil(NetworkService networkService) {
        this.networkService = networkService;
    }


    public String getNotificationMessage(Notification notification) {

        if (notification.getType() == null) {
            throw new InvalidNotificationException("Invalid notification type in notification");
        }

        if (notification.getType() == NotificationType.CONSENT_REQUEST) {
            return ConsentRequestNotificationMessageBuilder(notification);
        }

        if (notification.getType() == NotificationType.NETWORK_INVITATION) {
            return NetworkInvitationNotificationMessageBuilder(notification);
        }

        return null;
    }

    private String ConsentRequestNotificationMessageBuilder(Notification notification) {

        ConsentRequestBlock consentRequestBlock = (ConsentRequestBlock) notification.getReference();
        String requesterNetworkName;

        if (consentRequestBlock == null) {
            throw new InvalidNotificationException("Invalid or null Consent Request in notification");
        }

        try {
            requesterNetworkName = getConsentRequestNetworkName(consentRequestBlock);
        }
        catch (NullUserNetworkException Ex) {
            throw new InvalidNotificationException("Invalid Consent Request, caused by " + Ex.getMessage());
        }

        String message =
        notification.getSender().getUsername() + " from the " +
        requesterNetworkName + " network, " +
        " is asking for your consent to exchange your health record data.";

        return message;
    }

    private String NetworkInvitationNotificationMessageBuilder(Notification notification) {

        NetworkInvitationRequest invitationRequest = (NetworkInvitationRequest) notification.getReference();

        if (invitationRequest == null) {
            throw new InvalidNotificationException("Invalid or null Network Invitation Request in notification");
        }

        String message =
        notification.getSender().getUsername() + " from the " +
        invitationRequest.getNetworkName() +
        " [" + invitationRequest.getNetworkUUID() + "] " + " network, " +
        " has invited you to join their network.";

        return message;
    }

    private String getConsentRequestNetworkName(ConsentRequestBlock consentRequestBlock) {
        Network network;

        try {
            network = networkService.findNetwork(consentRequestBlock.getNetworkUUID());
        }
        catch (NullUserNetworkException Ex) {
            throw new NullUserNetworkException(Ex.getMessage() + " in consent request block");
        }

        return network.getName();
    }
}
