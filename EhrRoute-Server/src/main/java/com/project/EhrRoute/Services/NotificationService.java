package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.App.Notification;
import com.project.EhrRoute.Exceptions.BadRequestException;
import com.project.EhrRoute.Payload.App.PageResponse;
import com.project.EhrRoute.Repositories.NotificationRepository;
import com.project.EhrRoute.Security.CurrentUser;
import com.project.EhrRoute.Security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;


@Service
public class NotificationService
{
    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }


    @Transactional
    public void saveNotification(Notification notification)
    {
        notificationRepository.save(notification);
    }
}
