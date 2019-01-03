package com.project.EhrRoute.Repositories;
import com.project.EhrRoute.Entities.App.Notification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<Notification, Long>
{

}
