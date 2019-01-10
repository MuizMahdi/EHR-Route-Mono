import { NetworkInvitationRequest } from './../../../Models/Payload/Requests/NetworkInvitationRequest';
import { Component, OnInit } from '@angular/core';
import { NotificationService } from 'src/app/Services/notification.service';
import { NotificationsPageResponse } from 'src/app/Models/Payload/Responses/NotificationsResponse';
import { Notification } from 'src/app/Models/Payload/Responses/Notification';
import { NotificationType } from 'src/app/Models/Payload/NotificationType';


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

   // TODO: ADD CONSENT REQUEST NOTIFICATION [SERVER-END] AND ITS HANDLING [CLIENT-END]
   notificationMessageBuilder(notification:Notification): string
   {
      if (notification.notificationType === NotificationType.NETWORK_INVITATION) 
      {
         let invitationRequest:NetworkInvitationRequest = notification.reference;

         let message:string = 
         invitationRequest.senderUsername + " has invited you to join their network, " +
         invitationRequest.networkName;

         return message;
      }

      if (notification.notificationType === NotificationType.CONSENT_REQUEST) 
      {
         // TODO: ADD CONSENT REQUEST NOTIFICATION [SERVER-END] AND ITS HANDLING [CLIENT-END]
      }
   }

}
