package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.App.NetworkInvitationRequest;
import com.project.EhrRoute.Entities.App.Notification;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Entities.Core.ConsentRequestBlock;
import com.project.EhrRoute.Exceptions.BadRequestException;
import com.project.EhrRoute.Exceptions.InvalidNotificationException;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import com.project.EhrRoute.Models.NotificationType;
import com.project.EhrRoute.Models.PageConstants;
import com.project.EhrRoute.Payload.App.NotificationResponse;
import com.project.EhrRoute.Payload.App.PageResponse;
import com.project.EhrRoute.Repositories.NotificationRepository;
import com.project.EhrRoute.Security.CurrentUser;
import com.project.EhrRoute.Security.UserPrincipal;
import com.project.EhrRoute.Utilities.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class NotificationService
{
    private NotificationRepository notificationRepository;
    private UserService userService;
    private ModelMapper modelMapper;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserService userService, ModelMapper modelMapper) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }


    @Transactional
    public void saveNotification(Notification notification)
    {
        notificationRepository.save(notification);
    }


    @Transactional
    public PageResponse getCurrentUserNotifications(String username, int pageNumber, int pageSize) throws ResourceNotFoundException, InvalidNotificationException
    {
        // Validate page number and size constraints
        validatePageNumberAndSize(pageNumber, pageSize);

        // Create a pageable of the latest entries with size of 'size', sorted by creation date, in a descending direction.
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "createdAt");

        // Get currentUser User object
        User user = userService.findUserByUsernameOrEmail(username);

        if (user == null) {
            throw new ResourceNotFoundException("User", "username", username);
        }

        // Get a page of notifications using the pageable and current user as the notification recipient
        Page<Notification> notificationsPage = notificationRepository.findByRecipient(user, pageable);

        // If user has no notifications
        if (notificationsPage.getNumberOfElements() == 0) {
            // Then return an empty page response
            return new PageResponse<>(
                Collections.emptyList(),
                notificationsPage.getNumber(),
                notificationsPage.getSize(),
                notificationsPage.getTotalElements(),
                notificationsPage.getTotalPages(),
                notificationsPage.isFirst(),
                notificationsPage.isLast()
            );
        }

        // Get notifications list from notifications page
        List<Notification> notifications = notificationsPage.getContent();

        // Map Notifications to NotificationResponses
        List<NotificationResponse> notificationResponses = notifications.stream().map(notification -> {

            Object notificationReference = null;

            // Check for network invitation notifications
            if (notification.getType().equals(NotificationType.NETWORK_INVITATION))
            {
                // Get invitation request from notification's reference
                NetworkInvitationRequest invitationRequest = (NetworkInvitationRequest) notification.getReference();

                // recipient username is needed for the network invitation request payload
                String recipientUsername = notification.getRecipient().getUsername();

                if (invitationRequest == null || invitationRequest.getId() == null) {
                    throw new InvalidNotificationException("Invalid NetworkInvitationRequest notification reference.");
                }

                // Set the notification reference object as a UserConsentRequest
                notificationReference = modelMapper.mapNetworkInvitationRequestToPayload(invitationRequest, recipientUsername);
            }

            // Check for consent request notifications
            if (notification.getType().equals(NotificationType.CONSENT_REQUEST))
            {
                // Get ConsentRequest from notification's reference
                ConsentRequestBlock consentRequestBlock = (ConsentRequestBlock) notification.getReference();

                if (consentRequestBlock == null || consentRequestBlock.getId() == null) {
                    throw new InvalidNotificationException("Invalid ConsentRequestBlock notification reference.");
                }

                // Set the notification reference object as a UserConsentRequest
                notificationReference = modelMapper.mapConsentRequestBlockToUserConsentRequest(consentRequestBlock);
            }

            if (notificationReference == null) {
                throw new InvalidNotificationException("Invalid or null notification type");
            }

            return modelMapper.mapNotificationToNotificationResponse(
                notification.getSender(),
                notification.getRecipient(),
                notification.getType(),
                notificationReference
            );

        }).collect(Collectors.toList());

        // Return a page response with the notification responses
        return new PageResponse<>(
            notificationResponses,
            notificationsPage.getNumber(),
            notificationsPage.getSize(),
            notificationsPage.getTotalElements(),
             notificationsPage.getTotalPages(),
            notificationsPage.isFirst(),
            notificationsPage.isLast()
        );
    }


    private void validatePageNumberAndSize(int page, int size)
    {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less that zero");
        }

        if (size > PageConstants.MAX_NOTIFICATIONS_PAGE_SIZE) {
            throw new BadRequestException("Page size cannot not be greater than " + PageConstants.MAX_NOTIFICATIONS_PAGE_SIZE);
        }
    }
}
