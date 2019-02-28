import { ErrorResponse } from './../../../Models/Payload/Responses/ErrorResponse';
import { NetworkDetails } from './../../../Models/Payload/Responses/NetworkDetails';
import { NodeNetworkService } from 'src/app/Services/node-network.service';
import { NzModalService, NzModalRef } from 'ng-zorro-antd';
import { Notification } from './../../../Models/Payload/Responses/Notification';
import { NotificationService } from './../../../Services/notification.service';
import { UpdateConsentRequest } from './../../../Models/Payload/Responses/UpdateConsentRequest';
import { Component, OnInit, Input } from '@angular/core';


@Component({
  selector: 'app-update-consent-request',
  templateUrl: './update-consent-request.component.html',
  styleUrls: ['./update-consent-request.component.css']
})


export class UpdateConsentRequestComponent implements OnInit 
{
   @Input() notification: Notification;
   updateConsentRequest: UpdateConsentRequest;
   requesterNetworkDetails: NetworkDetails;


   constructor(
      private notificationService: NotificationService, private modalService: NzModalService,
      private networkService: NodeNetworkService, private modal: NzModalRef
   ) { }


   ngOnInit() {
      if (this.notification) {
         this.updateConsentRequest = this.notification.reference;
         this.getRequesterNetworkDetails(this.updateConsentRequest.userConsentRequest.networkUUID);
      }
   }


   getRequesterNetworkDetails(networkUUID:string): void
   {
      this.networkService.getNetworkDetails(networkUUID).subscribe(

         (response:NetworkDetails) => {
            this.requesterNetworkDetails = response;
         },

         (error:ErrorResponse) => {
            console.log(error);
         }

      );
   }


   async onConsentRequestAccept()
   {

   }


   onConsentRequestReject() 
   {
      // View a modal asking for confirmation
      this.modalService.confirm({
         nzTitle: 'Are you sure that you want to reject ?',
         nzContent: 'The institution will not be notified if you reject the request',
         nzOnOk: () => { this.deleteNotification(); this.modal.close(); }
      });
   }


   deleteNotification(): void 
   {
      this.notificationService.deleteNotification(this.notification.notificationID).subscribe( 

         response => {
            console.log(response);
         },

         error => {
            console.log(error);
         }

      );
   }
}
