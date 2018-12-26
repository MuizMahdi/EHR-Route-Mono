import { HttpClient } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { EventSourcePolyfill } from 'ng-event-source';
import { environment } from 'src/environments/environment';
import { catchError, first } from 'rxjs/operators';
import { throwError, Observable } from 'rxjs';



@Injectable({
  providedIn: 'root'
})


/*
*   This servie handles all node clusters(groups) server sent events end points
*   subscription and SSEs received.
*/


export class NodeClustersService implements OnInit
{
   providerSubscriptionUrl:string = environment.apiUrl + "/cluster/chainprovider?nodeuuid=";
   consumerSubscriptionUrl:string = environment.apiUrl + "/cluster/chainconsumer?nodeuuid=";
   clustersUnsubscripeUrl:string = environment.apiUrl + "/cluster/close?nodeuuid=";

   providersEventSource:EventSourcePolyfill;
   consumersEventSource:EventSourcePolyfill;
   

   constructor(private http:HttpClient) { }

   ngOnInit() {
     this.unsubscribeClusters();
   }


   subscribeProvider(): void
   {
      let nodeUUID:string = "a906c224-f882-4cc7-bf48-31ece53765fa";
      let url:string = this.providerSubscriptionUrl + nodeUUID;

      let Jwt = localStorage.getItem('accessToken');

      this.providersEventSource = new EventSourcePolyfill(url, {headers: {Authorization: "Bearer " + Jwt}});

      this.providersEventSource.onmessage = ((event:any) => {

         console.log(event.data);

      });

   }

   
   subscribeConsumer(): void
   {
      let nodeUUID:string = "a906c224-f882-4cc7-bf48-31ece53765fa";
      let url:string = this.consumerSubscriptionUrl + nodeUUID;

      let Jwt = localStorage.getItem('accessToken');

      this.consumersEventSource = new EventSourcePolyfill(url, {headers: {Authorization: "Bearer " + Jwt}});

      this.consumersEventSource.onmessage = ((event:any) => {

         console.log(event.data);

      });
   }

   
   unsubscribeClusters(): void
   {
      this.closeSseConnection();
      
      let nodeUUID = "a906c224-f882-4cc7-bf48-31ece53765fa";

      this.http.get(this.clustersUnsubscripeUrl + nodeUUID).pipe(first(),
         
         catchError(error => {
            return throwError(error)
         })
         
      ).subscribe(
         res => {
            console.log(res);
         },

         err => {
            console.log(err);
         }
      );
   }


   closeSseConnection():void 
   {
      if (this.providersEventSource) 
      {
         if (this.providersEventSource.OPEN || this.providersEventSource.CONNECTING) {
            this.providersEventSource.close();
         }
      }

      if (this.consumersEventSource) 
      {
         if (this.consumersEventSource.OPEN || this.consumersEventSource.CONNECTING) {
            this.consumersEventSource.close();
         }
      } 
   }


}
