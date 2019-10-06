package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Models.PageConstants;
import com.project.EhrRoute.Payload.App.PageResponse;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Security.CurrentUser;
import com.project.EhrRoute.Security.UserPrincipal;
import com.project.EhrRoute.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/notifications")
public class NotificationController
{
    private NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @GetMapping("/current-user")
    public ResponseEntity getCurrentUserNotifications(
            @CurrentUser UserPrincipal currentUser,
            @RequestParam(value = "page", defaultValue = PageConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = PageConstants.DEFAULT_NOTIFICATIONS_PAGE_SIZE) int size )
    {
        PageResponse userNotifications;

        try {
            userNotifications = notificationService.getCurrentUserNotifications(
                currentUser.getAddress(), page, size
            );
        }
        catch (Exception Ex) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, Ex.getMessage()));
        }

        return ResponseEntity.ok(userNotifications);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteNotification(@PathVariable("id") Long id)
    {
        try {
            notificationService.deleteNotification(id);
        }
        // If resource doesn't exist
        catch (Exception Ex) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse(true, "Notification doesn't exist"));
        }

        return ResponseEntity.ok(new ApiResponse(true, "Notification has been deleted"));
    }
}
