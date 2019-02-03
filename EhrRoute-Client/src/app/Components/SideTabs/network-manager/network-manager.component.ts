import { SimpleStringResponse } from './../../../Models/Payload/Responses/SimpleStringResponse';
import { ErrorResponse } from './../../../Models/Payload/Responses/ErrorResponse';
import { BlockResponse } from './../../../Models/Payload/Responses/BlockResponse';
import { UserNetworks } from '../../../Models/Payload/Responses/UserNetworks';
import { NodeNetworkService } from './../../../Services/node-network.service';
import { UserRole } from './../../../Models/UserRole';
import { MainLayoutService } from './../../../Services/main-layout.service';
import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/Services/auth.service';
import { NetworkInfo } from 'src/app/Models/Payload/Responses/NetworkInfo';
import { NzModalService } from 'ng-zorro-antd';
import { NodeClustersService } from 'src/app/Services/node-clusters.service';
import { NetworkInvitationRequest } from 'src/app/Models/Payload/Requests/NetworkInvitationRequest';
import { DatabaseService } from 'src/app/DataAccess/database.service';
import ModelMapper from 'src/app/Helpers/Utils/ModelMapper';


@Component({
  selector: 'app-network-manager',
  templateUrl: './network-manager.component.html',
  styleUrls: ['./network-manager.component.css']
})


export class NetworkManagerComponent implements OnInit 
{
   isAdmin:boolean = false;
   isProvider:boolean = false;

   selectedNetwork:any = {};
   selectedNetworkUUID:string;
   userNetworks:NetworkInfo[];
   userHasNetwork:boolean = false;

   newNetworkName:string;
   isNetworkCreationModalVisible:boolean = false;

   invitedUserUsername:string;


   constructor(
      private nodeNetworkService:NodeNetworkService, private authService:AuthService, 
      public mainLayout:MainLayoutService, private modalService:NzModalService,
      private nodeClusterService:NodeClustersService, private databaseService:DatabaseService
   ) { }


   ngOnInit():void 
   {
      this.mainLayout.show();
      this.initUserRole();
      this.getUserNetworks();
   }


   initUserRole():void 
   {
      // Get user roles
      this.authService.getCurrentUserRoles().subscribe(

         (roles:UserRole[]) => {

            // Iterate through the roles array and set role flags
            roles.forEach(role => {
               if (role.roleName.trim() === 'ROLE_ADMIN') this.isAdmin = true; 
               if (role.roleName.trim() === 'ROLE_PROVIDER') this.isProvider = true;
            });

            console.log('ADMIN: ' + this.isAdmin);

         },

         errorResponse => {
            console.log(errorResponse);
         }

      );
   }


   getUserNetworks():void 
   {
      this.nodeNetworkService.getUserNetworks().subscribe(

         (response:UserNetworks) => {
            // If network response is received then user has a network or more.
            this.userHasNetwork = true;

            // Networks the user joined
            this.userNetworks = response.userNetworks;

            // Select the first network as default
            this.selectedNetwork = this.userNetworks[0];

            // Currently selected network UUID
            this.selectedNetworkUUID = this.selectedNetwork.networkUUID;

            // Ensure that a connection is established for each network's database
            this.ensureNetworksDBsConnect(this.userNetworks);
         },

         (error:ErrorResponse) => {
            console.log(error);

            // If Http NOT_FOUND status is returned
            if (error.httpStatus === 404) {
               this.userHasNetwork = false;
            }
         }

      );
   }


   ensureNetworksDBsConnect(userNetworks:NetworkInfo[])
   {
      if (userNetworks.length > 0)
      {
         userNetworks.forEach(async network => {

            try 
            {
               await this.databaseService.createNetworkDbConnection(network.networkUUID);
            }
            catch(error)
            {
               // If a connection for the network has already been established before
               if ( (<Error>error).name == 'AlreadyHasActiveConnectionError' ) {
                  return;
               }
               else {
                  console.log(error);
               }
            
            }
            
         });
      }
   }


   generateNetwork(networkName:string):void
   {

      this.nodeNetworkService.generateNetwork(networkName).subscribe(

         (response:BlockResponse) => {            
            // Save the received genesis block
            this.saveNetworkGenesisBlock(networkName, response);

            // Update page contents with the newly added network
            // Also establishes a connection to the newly added network via ensureNetworksDBsConnect()
            //this.getUserNetworks();

            //TODO: Fix bug when calling getUserNetworks() after saving block on DB
         },

         (error:ErrorResponse) => {
            console.log(error);
         }

      );

   }


   private saveNetworkGenesisBlock(networkName:string, genesisBlock:BlockResponse): void
   {
      // Get network UUID of network with network name of the recently created network
      this.nodeNetworkService.getNetworkUuidByName(networkName).subscribe(

         async (response:SimpleStringResponse) => {
            // UUID response
            let networkUUID = response.payload;
            
            // Create a DB connection for the recently added network
            await this.databaseService.createNetworkDbConnection(networkUUID);

            // Get a Block from the response genesis block
            let block = ModelMapper.mapBlockResponseToBlock(genesisBlock);

            // Get DB connection for the network, then save the block
            await this.databaseService.getNetworkDbConnection(networkUUID).manager.save(block);
         },

         (error:ErrorResponse) => {
            console.log(error);
         }

      );

   }


   log(value:any): void 
   {
      console.log(value);
   }


   showNetworkCreationModal(): void 
   {
      this.isNetworkCreationModalVisible = true;
   }


   onNetworkCreationSubmit(): void 
   {
      // Generate network using the network name input value
      this.generateNetwork(this.newNetworkName);

      // Close modal
      this.isNetworkCreationModalVisible = false;
   }

  
   onNetworkCreationCancel(): void 
   {
      this.isNetworkCreationModalVisible = false;
   }


   inviteUser(username:string): void 
   {
      let currentUserUsername = this.authService.getCurrentUser().username;

      let invitationRequest:NetworkInvitationRequest = {
         recipientUsername: username,
         senderUsername: currentUserUsername,
         networkName: this.selectedNetwork.name,
         networkUUID: this.selectedNetwork.networkUUID,
         invitationToken: null // Invitation token is created on server-side
      }

      this.nodeNetworkService.sendNetworkInvitationRequest(invitationRequest).subscribe(

         response => {
            console.log(response);
         },

         error => {
            console.log(error);
         }

      );

   }
}
