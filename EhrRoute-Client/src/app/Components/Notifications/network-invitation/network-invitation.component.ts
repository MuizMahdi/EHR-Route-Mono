import { ErrorResponse } from './../../../Models/Payload/Responses/ErrorResponse';
import { ProviderService } from './../../../Services/provider.service';
import { ChainService } from './../../../Services/chain.service';
import { AuthService } from './../../../Services/auth.service';
import { Component, OnInit, Input } from '@angular/core';
import { Notification } from 'src/app/Models/Payload/Responses/Notification';
import { NzModalRef } from 'ng-zorro-antd';
import { NetworkInvitationRequest } from 'src/app/Models/Payload/Requests/NetworkInvitationRequest';
import { NotificationService } from 'src/app/Services/notification.service';
import { NodeNetworkService } from 'src/app/Services/node-network.service';


@Component({
  selector: 'app-network-invitation',
  templateUrl: './network-invitation.component.html',
  styleUrls: ['./network-invitation.component.css']
})


export class NetworkInvitationComponent implements OnInit 
{
   
   @Input() notification: Notification;
   invitationRequest:NetworkInvitationRequest;


   constructor(
      private modal:NzModalRef, private notificationService:NotificationService, 
      private authService:AuthService, private networkService:NodeNetworkService,
      private chainService:ChainService, private providerService:ProviderService
   ) 
   { }


   ngOnInit() {
      if (this.notification) {
         this.invitationRequest = this.notification.reference;
      }
   }


   onInvitationAccept(): void
   {
      if (this.notification) 
      {
         this.invitationAccept(this.invitationRequest);
         //this.deleteNotification();
      }

      this.modal.destroy();
   }
   

   invitationAccept(invitationRequest:NetworkInvitationRequest): void
   {
      this.getNetworkChainFile(invitationRequest.networkUUID);

      /*
      this.networkService.networkInvitationAccept(this.invitationRequest).subscribe(

         response => {
            this.getNetworkChainFile(invitationRequest.networkUUID);
         },

         error => {
            console.log(error);
         }

      );
      */
   }


   private getNetworkChainFile(networkUUID:string)
   {
      this.getCurrentProviderUUID().then(providerUUID => {
         this.chainService.getNetworkChain(providerUUID, networkUUID).subscribe(
            response => {
               console.log(response);
            },

            (error:ErrorResponse) => {
               console.log(error);
            }
         )
      })
   }


   private async getCurrentProviderUUID(): Promise<string>
   {
      let providerUUID:string;
      
      await this.providerService.getCurrentProviderUUID().then(
         
         response => {
            providerUUID = response.payload;
         })

         .catch(error => {
            console.log(error);
         }

      );
      
      return providerUUID;
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
