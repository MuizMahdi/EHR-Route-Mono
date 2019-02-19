import { ErrorResponse } from 'src/app/Models/Payload/Responses/ErrorResponse';
import { NetworkDetails } from './../../../Models/Payload/Responses/NetworkDetails';
import { NodeNetworkService } from './../../../Services/node-network.service';
import { NotificationService } from './../../../Services/notification.service';
import { Notification } from 'src/app/Models/Payload/Responses/Notification';
import { NzModalRef } from 'ng-zorro-antd';
import { ConsentRequest } from './../../../Models/Payload/Responses/ConsentRequest';
import { Component, OnInit, Input } from '@angular/core';


@Component({
  selector: 'app-consent-request',
  templateUrl: './consent-request.component.html',
  styleUrls: ['./consent-request.component.css']
})


export class ConsentRequestComponent implements OnInit 
{

   @Input() notification: Notification;
   consentRequest: ConsentRequest;
   requesterNetworkDetails: NetworkDetails;


   constructor(
      private notificationService:NotificationService,
      private networkService:NodeNetworkService,
      private modal:NzModalRef,
   ) 
   { }


   ngOnInit() {
      if (this.notification) {
         this.consentRequest = this.notification.reference;
         this.getRequesterNetworkDetails(this.consentRequest.networkUUID);
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


   onConsentRequestAccept(): void
   {
      // Add user address, private key, and info into the Block in the ConsentRequest
      
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
