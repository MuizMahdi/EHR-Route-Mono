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


    public String getNotificationMessage(Notification notification) throws InvalidNotificationException
    {
        if (notification.getType() == null)
        {
            throw new InvalidNotificationException("Invalid notification type in notification");
        }

        if (notification.getType() == NotificationType.CONSENT_REQUEST)
        {
            return ConsentRequestNotificationMessageBuilder(notification);
        }

        if (notification.getType() == NotificationType.NETWORK_INVITATION)
        {
            return NetworkInvitationNotificationMessageBuilder(notification);
        }

        // Returns null if something wrong happens
        return null;
    }

    private String ConsentRequestNotificationMessageBuilder(Notification notification) throws InvalidNotificationException
    {
        ConsentRequestBlock consentRequestBlock = (ConsentRequestBlock) notification.getReference();
        String requesterNetworkName;

        if (consentRequestBlock == null)
        {
            throw new InvalidNotificationException("Invalid or null Consent Request in notification");
        }

        try
        {
            requesterNetworkName = getConsentRequestNetworkName(consentRequestBlock);
        }
        catch (NullUserNetworkException Ex)
        {
            throw new InvalidNotificationException("Invalid Consent Request, caused by " + Ex.getMessage());
        }

        String message =
        notification.getSender().getUsername() + " from the " +
        requesterNetworkName + " network, " +
        " is asking for your consent to exchange your health record data.";

        return message;
    }

    private String NetworkInvitationNotificationMessageBuilder(Notification notification) throws InvalidNotificationException
    {
        NetworkInvitationRequest invitationRequest = (NetworkInvitationRequest) notification.getReference();

        if (invitationRequest == null)
        {
            throw new InvalidNotificationException("Invalid or null Network Invitation Request in notification");
        }

        String message =
        notification.getSender().getUsername() + " from the " +
        invitationRequest.getNetworkName() +
        " [" + invitationRequest.getNetworkUUID() + "] " + " network, " +
        " has invited you to join their network.";

        return message;
    }

    private String getConsentRequestNetworkName(ConsentRequestBlock consentRequestBlock) throws NullUserNetworkException
    {
        Network network = networkService.findByNetUUID(consentRequestBlock.getNetworkUUID());

        if (network == null || network.getName().isEmpty() || network.getName() == null)
        {
            throw new NullUserNetworkException("Invalid network with UUID: [" + consentRequestBlock.getNetworkUUID() + "] in consent request block");
        }

        return network.getName();
    }
}
