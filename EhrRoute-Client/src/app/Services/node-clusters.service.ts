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


   subscribeClusters(): void
   {
      // Subscribe node as a provider
      this.subscribeProvider();

      // Subscribe node as a consumer
      this.subscribeConsumer();
   }

   subscribeProvider(): void
   {
      let nodeUUID:string = "a906c224-f882-4cc7-bf48-31ece53765fa";
      let url:string = this.providerSubscriptionUrl + nodeUUID;

      let Jwt = localStorage.getItem('accessToken');

      if (!this.providersEventSource) 
      {
         console.log("SENDING PROVDER SUBSCRIBE REQUEST");

         this.providersEventSource = new EventSourcePolyfill(
            url, 
            {headers: {Authorization: "Bearer " + Jwt}}
         );

         this.providersEventSource.onmessage = ((event:any) => {

            console.log(event.data);

         });
      }

   }

   
   subscribeConsumer(): void
   {
      let nodeUUID:string = "a906c224-f882-4cc7-bf48-31ece53765fa";
      let url:string = this.consumerSubscriptionUrl + nodeUUID;

      let Jwt = localStorage.getItem('accessToken');

      if (!this.consumersEventSource)
      {
         console.log("SENDING CONSUMER SUBSCRIBE REQUEST");

         this.consumersEventSource = new EventSourcePolyfill(
            url, 
            {headers: {Authorization: "Bearer " + Jwt}}
         );

         this.consumersEventSource.onmessage = ((event:any) => {

            console.log(event.data);

         });
      }   
   }

   
   unsubscribeClusters(): void
   {
      console.log("[ClusterService] Sending unsubscribe request...");

      let nodeUUID = "a906c224-f882-4cc7-bf48-31ece53765fa";

      // Unsubscribe node from clusters
      this.http.get(this.clustersUnsubscripeUrl + nodeUUID).pipe(first(),
         
         catchError(error => {
            return throwError(error)
         })
         
      ).subscribe(
         res => {
            console.log("[ClusterService] Node has unsubscribed from clusters successfully.");
            // Close the SSE http connection afterwards
            this.closeSseConnection();
         },

         err => {
            console.log(err);
         }
      );
   }


   closeSseConnection():void 
   {
      // If event source exists
      if (this.providersEventSource) 
      {
         // And connection is open 
         if (this.providersEventSource.OPEN || this.providersEventSource.CONNECTING) {

            // Close the connection
            this.providersEventSource.close();

            // Set event source as undefined so we could subscribe again
            this.providersEventSource = undefined;

            console.log("[ClusterService] Providers SSE connection has been closed successfully.");
         }
      }

      if (this.consumersEventSource) 
      {
         if (this.consumersEventSource.OPEN || this.consumersEventSource.CONNECTING) {

            this.consumersEventSource.close();

            this.consumersEventSource = undefined;
            
            console.log("[ClusterService] Consumers SSE connection has been closed successfully.");
         }
      } 
   }


}
