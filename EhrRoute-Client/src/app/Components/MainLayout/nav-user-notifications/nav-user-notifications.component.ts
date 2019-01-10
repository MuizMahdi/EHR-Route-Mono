import { Component, OnInit } from '@angular/core';
import { NotificationService } from 'src/app/Services/notification.service';
import { NotificationsPageResponse } from 'src/app/Models/Payload/Responses/NotificationsResponse';
import { Notification } from 'src/app/Models/Payload/Responses/Notification';


@Component({
  selector: 'app-nav-user-notifications',
  templateUrl: './nav-user-notifications.component.html',
  styleUrls: ['./nav-user-notifications.component.css']
})


export class NavUserNotificationsComponent implements OnInit 
{
   isNotificationModalVisible:boolean = false;
   isNavNotificationsMenuVisible:boolean = false;

   notifications:Notification[];
   notificationsResponse:NotificationsPageResponse;


   constructor(private notificationService:NotificationService) { }
   

   ngOnInit() 
   { 
      this.notificationService.getUserNotifications().subscribe(

         response => {
            this.notificationsResponse = response;
            this.notifications = this.notificationsResponse.resources;
         },

         error => {
            console.log(error);
         }

      );
   }


   /* Notificaiton Menu Methods */
   onClickedOutside(e: Event) 
   {
      this.isNavNotificationsMenuVisible = false;
   }


   /* Notificaiton Methods */
   onNotificationClick(notification:Notification) 
   {
      console.log(notification.notificationType);
   }

   fa(notification:Notification):string
   {
      return "FA!" + notification.notificationType;
   }


   /* Notificaiton Modal Methods */

   showNotificationModal(): void {
      this.isNotificationModalVisible = true;
   }

   handleNotificationOk(): void {
      this.isNotificationModalVisible = false;
   }

   handleNotificationIgnore(): void {
      this.isNotificationModalVisible = false;
   }

}
