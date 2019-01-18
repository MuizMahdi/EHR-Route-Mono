import { DatabaseService } from './../../DataAccess/database.service';
import { ErrorResponse } from './../../Models/Payload/Responses/ErrorResponse';
import { NetworkInfo } from './../../Models/Payload/Responses/NetworkInfo';
import { UserNetworks } from './../../Models/Payload/Responses/UserNetworks';
import { MainLayoutService } from './../../Services/main-layout.service';
import { Component, OnInit } from '@angular/core';
import { NodeClustersService } from 'src/app/Services/node-clusters.service';
import { NodeNetworkService } from 'src/app/Services/node-network.service';


@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})


export class MainComponent implements OnInit
{
   
   constructor(public mainLayout:MainLayoutService, private clustersService:NodeClustersService,
   private nodeNetworkService:NodeNetworkService, private databaseService:DatabaseService) 
   { 
      this.mainLayout.show();
   }


   ngOnInit() 
   {
      // Handles when user reloads page after loggin in, to show a prompt, which 
      // allows for a request to be made, unsubscribing the node from clusters.
      //this.handleReloads();

      // Subscribe to providers and consumers cluster
      //this.clustersService.subscribeProvider();
      //this.clustersService.subscribeConsumer();

      // Establishes a connection for each user network database
      this.initializeNetworksDBs();
   }


   handleReloads(): void
   {
      var showMsgTimer;
      var clusterService = this.clustersService;

      // Before user refreshes page, show a prompt asking for confirmation
      window.onbeforeunload = function(evt) {       
         
         // Unsubscribe from clusters and close SSE http connections (EventSource connections)
         clusterService.unsubscribeClusters();

         showMsgTimer = window.setTimeout(showMessage, 500);
 
         evt.returnValue = '';
     
         return '';
      }
     
      window.onunload = function () {
         clearTimeout(showMsgTimer);
      }

      // If user decides to stay on page
      function showMessage() {
         // Subscribe to clusters again
         clusterService.subscribeClusters();
      }
   }


   async initializeNetworksDBs()
   {
      // Get logged in user's networks
      this.nodeNetworkService.getUserNetworks().subscribe(
        
         (response:UserNetworks) => {

            let userNetworks:NetworkInfo[] = response.userNetworks;

            // If the user has networks
            if (userNetworks.length > 0) 
            {
               // Establish a connection for each network DB (creates a DB file if its first time)
               userNetworks.forEach(async network => {
                  await this.databaseService.createNetworkDbConnection(network.networkUUID);
               });
            }

         },

         (error:ErrorResponse) => {
            // If no networks are found for user
            if (error.httpStatus === 404) {
               console.log("[ User has no networks ]");
               // TODO: Set the network-manager userHasNetworks boolean to false
            }
         }

      );
   }

}
