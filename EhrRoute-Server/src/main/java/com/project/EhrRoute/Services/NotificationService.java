package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.App.Notification;
import com.project.EhrRoute.Repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
