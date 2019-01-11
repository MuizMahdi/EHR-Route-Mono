import { Component, OnInit, Input } from '@angular/core';
import { Notification } from 'src/app/Models/Payload/Responses/Notification';
import { NzModalRef } from 'ng-zorro-antd';
import { NetworkInvitationRequest } from 'src/app/Models/Payload/Requests/NetworkInvitationRequest';
import { NotificationService } from 'src/app/Services/notification.service';


@Component({
  selector: 'app-network-invitation',
  templateUrl: './network-invitation.component.html',
  styleUrls: ['./network-invitation.component.css']
})


export class NetworkInvitationComponent implements OnInit 
{
   @Input() notification: Notification;
   invitationRequest:NetworkInvitationRequest;


   constructor(private notificationService:NotificationService, private modal:NzModalRef) { }


   ngOnInit() {

      if (this.notification) {
         this.invitationRequest = this.notification.reference;
      }

   }


   public onInvitationAccept() {

      console.log("Invitation accepted.");
      this.modal.destroy();
   }   

}
