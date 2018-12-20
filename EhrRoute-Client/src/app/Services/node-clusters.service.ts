import { HttpClient } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';


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

}
