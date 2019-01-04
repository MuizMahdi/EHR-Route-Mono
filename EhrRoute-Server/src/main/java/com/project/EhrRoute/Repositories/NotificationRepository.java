package com.project.EhrRoute.Repositories;
import com.project.EhrRoute.Entities.App.Notification;
import com.project.EhrRoute.Entities.Auth.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<Notification, Long>
{
    // TODO(MAYBE): FIND USER NOTIFICATION BY NOTIFICATION TYPE
    Page<Notification> findByRecipient(User recipient, Pageable pageable);
}
