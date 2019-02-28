import { UpdateConsentRequestComponent } from './../../Notifications/update-consent-request/update-consent-request.component';
import { UpdateConsentRequest } from './../../../Models/Payload/Responses/UpdateConsentRequest';
import { ConsentRequestComponent } from './../../Notifications/consent-request/consent-request.component';
import { ConsentRequest } from './../../../Models/Payload/Responses/ConsentRequest';
import { NetworkInvitationComponent } from '../../Notifications/network-invitation/network-invitation.component';
import { NetworkInvitationRequest } from './../../../Models/Payload/Requests/NetworkInvitationRequest';
import { Component, OnInit } from '@angular/core';
import { NotificationService } from 'src/app/Services/notification.service';
import { NotificationsPageResponse } from 'src/app/Models/Payload/Responses/NotificationsResponse';
import { Notification } from 'src/app/Models/Payload/Responses/Notification';
import { NotificationType } from 'src/app/Models/NotificationType';
import { NzModalService } from 'ng-zorro-antd'; 


@Component({
  selector: 'app-nav-user-notifications',
  templateUrl: './nav-user-notifications.component.html',
  styleUrls: ['./nav-user-notifications.component.css']
})


export class NavUserNotificationsComponent implements OnInit 
{
   isNotificationModalVisible:boolean = false;
   isNavNotificationsMenuVisible:boolean = false;
   hasNewNotifications:boolean;

   notifications:Notification[];
   notificationsResponse:NotificationsPageResponse;


   constructor(private notificationService:NotificationService, private modalService: NzModalService) { }
   

   ngOnInit() 
   { 
      this.getUserNotifications();
   }


   getUserNotifications(): void
   {
      this.notificationService.getUserNotifications().subscribe(

         response => {
            
            this.notificationsResponse = response;

            this.notifications = this.notificationsResponse.resources;
            
            if (this.notifications.length == 0) {
               this.hasNewNotifications = false;
            }
            else {
               this.hasNewNotifications = true;
            }

         },

         error => {
            console.log(error);
         }

      );
   }


   /* Notificaiton Menu Methods */

   onClickedOutside(e: Event): void
   {
      this.isNavNotificationsMenuVisible = false;
   }


   /* Notificaiton Methods */

   onNotificationClick(notification:Notification): void
   {
      switch(notification.notificationType) {

         case NotificationType.NETWORK_INVITATION: {
            let title = "Network Invitation Request";

            // View the NetworkInviteNotification component within modal
            this.createNotificationComponentModal(
               notification, title, NetworkInvitationComponent
            );

            break;
         }

         case NotificationType.CONSENT_REQUEST: {
            let title = "Medical Record Exchange Consent Request";

            // View the Consent Request info within modal
            this.createNotificationComponentModal(
               notification, title, ConsentRequestComponent
            );

            break;
         }

         case NotificationType.UPDATE_CONSENT_REQUEST: {
            let title = "Medical Record Update Consent Request";

            // View the Update Consent Request info within modal
            this.createNotificationComponentModal(
               notification, title, UpdateConsentRequestComponent
            );

            break;
         }

         default: { 
            break; 
         }

      }
   }


   // Todo: add consent request notification
   notificationMessageBuilder(notification:Notification): string
   {
      switch(notification.notificationType) {

         case NotificationType.NETWORK_INVITATION: {
            let invitationRequest:NetworkInvitationRequest = notification.reference;

            let message:string =
            invitationRequest.senderUsername + " has invited you to join their network, " +
            invitationRequest.networkName;
   
            return message;
         }

         case NotificationType.CONSENT_REQUEST: {
            let ehrExchangeConsentRequest: ConsentRequest = notification.reference;
         
            let message:string =
            notification.senderName + 
            " is asking for consent to use and share your medical record";
   
            return message;
         }

         case NotificationType.UPDATE_CONSENT_REQUEST: {
            let ehrExchangeConsentRequest: UpdateConsentRequest = notification.reference;
         
            let message:string =
            notification.senderName + 
            " is asking for consent to update or make a change in your medical record";

            return message;
         }

         default: { 
            break; 
         }

      }
   }


   /* Notificaiton Modal Methods */

   createNotificationComponentModal(notification:Notification, notificationTypeTitle:string, notificationComponent:any): void 
   {
      const notificationModal = this.modalService.create({

         nzTitle: notificationTypeTitle,

         nzContent: notificationComponent,

         // Pass the notification object to the component as an input
         nzComponentParams: {
            notification: notification
         },

         nzWidth: '50vw',

         nzFooter: [
            {
               label: 'Later',
               shape: 'default',
               onClick: () => notificationModal.destroy()
            }
         ]

      });

      // delay until modal instance created
      window.setTimeout(() => {
        const instance = notificationModal.getContentComponent();
      }, 2000);
   }

}
