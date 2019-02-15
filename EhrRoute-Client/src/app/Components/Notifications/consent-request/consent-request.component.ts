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


   constructor(private notificationService:NotificationService, private modal:NzModalRef) 
   { }


   ngOnInit() {
      if (this.notification) {
         this.consentRequest = this.notification.reference;
      }
   }


   onConsentRequestAccept(): void
   {
      
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
