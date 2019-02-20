import { RoleName } from './../../../Models/RoleName';
import { InformationInputComponent } from './../../Modals/information-input/information-input.component';
import { NzModalService } from 'ng-zorro-antd';
import { UserInfo } from './../../../Models/Payload/Responses/UserInfo';
import { AuthService } from 'src/app/Services/auth.service';
import { AddressService } from './../../../Services/address.service';
import { Component, OnInit } from '@angular/core';
import { NodeNetworkService } from 'src/app/Services/node-network.service';
import { MainLayoutService } from './../../../Services/main-layout.service';
import { NodeClustersService } from 'src/app/Services/node-clusters.service';


@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})


export class MainComponent implements OnInit
{
   constructor(
      public mainLayout:MainLayoutService, private clustersService:NodeClustersService,
      private networkService:NodeNetworkService, private addressService:AddressService,
      private authService:AuthService, private modalService:NzModalService
   ) {
      this.mainLayout.show();
   }


   ngOnInit()
   {
      // Handles when user reloads page after loggin in, to show a prompt, which 
      // allows for a request to be made, unsubscribing the node from clusters.
      //this.handleReloads();

      
      this.checkIfUserIsProvider();
      this.initializeLocalDbs();
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


   private initializeLocalDbs(): void
   {
      // If user has provider role
      if (this.authService.isUserProvider()) {

         // Establish connection to user's address DB
         this.addressService.ensureAddressDBsConnection();

         // Establish connections to all of user's networks DBs if they exist
         this.networkService.checkUserNetworks();
      }
   }


   // Checks if user has a 'Provider' role
   private checkIfUserIsProvider(): void
   {
      // Get user info once received from server
      this.authService.currentUser.subscribe((userInfo:UserInfo) => {

         let isProvider:boolean = false;

         // If user is logged in and user info received
         if (userInfo) {

            // Go through the user roles
            userInfo.roles.forEach(role => {
               // If user has a 'Provider' role
               if (role === RoleName.PROVIDER) {
                  isProvider = true;
               }
            });

            // If user is not a provider, then check if they have added and saved their info
            if (!isProvider) {
               this.checkIfHasAddedInfo(userInfo);
            }

         }

      });
   }


   private checkIfHasAddedInfo(userInfo:UserInfo): void
   {
      // If user hasven't submitted their info
      if (userInfo && !(userInfo.hasAddedInfo)) {
         this.showUserInfoModal();
      }
   }


   private showUserInfoModal(): void
   {
      const userInfoModal = this.modalService.create({
         nzTitle: "Add your personal information",
         nzContent: InformationInputComponent,
         nzWidth: "70%",
         nzFooter: null,
         nzClosable: false,
         nzMaskClosable: false,
         nzKeyboard: false
      });

      // delay until modal instance created
      window.setTimeout(() => {
        const instance = userInfoModal.getContentComponent();
      }, 2000);
   }
}
