import { HttpClient } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import {EventSourcePolyfill} from 'ng-event-source';


@Injectable({
  providedIn: 'root'
})


/*
*   This servie handles all node clusters(groups) server sent events end points
*   subscription and SSEs received.
*/


export class NodeClustersService implements OnInit
{
   
   constructor(private http:HttpClient) { }

   ngOnInit() {
     
   }


   subscribeProvider() 
   {
      let nodeUUID:string = "a906c224-f882-4cc7-bf48-31ece53765fa";
      let url:string = "http://localhost:8080/cluster/chainprovider?nodeuuid=" + nodeUUID;

      let Jwt = localStorage.getItem('accessToken');

      let eventSource = new EventSourcePolyfill(url, {headers: {Authorization: "Bearer " + Jwt}});

      eventSource.onmessage = ((event:any) => {

         console.log(event.data);

      });
   }

   
   subscribeConsumer()
   {
      let nodeUUID:string = "a906c224-f882-4cc7-bf48-31ece53765fa";
      let url:string = "http://localhost:8080/cluster/chainconsumer?nodeuuid=" + nodeUUID;

      let Jwt = localStorage.getItem('accessToken');

      let eventSource = new EventSourcePolyfill(url, {headers: {Authorization: "Bearer " + Jwt}});

      eventSource.onmessage = ((event:any) => {

         console.log(event.data);

      });
   }


}
