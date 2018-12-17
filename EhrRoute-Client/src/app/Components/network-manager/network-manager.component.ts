import { UserInfo } from './../../Models/UserInfo';
import { NodeNetworkService } from './../../Services/node-network.service';
import { UserRole } from './../../Models/UserRole';
import { MainLayoutService } from './../../Services/main-layout.service';
import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/Services/auth.service';
import { NetworkInfo } from 'src/app/Models/NetworkInfo';
import { NzModalService } from 'ng-zorro-antd';


@Component({
  selector: 'app-network-manager',
  templateUrl: './network-manager.component.html',
  styleUrls: ['./network-manager.component.css']
})


export class NetworkManagerComponent implements OnInit 
{
   isAdmin:boolean = false;
   isProvider:boolean = false;
   networkInfo:NetworkInfo;


   constructor(
      private nodeNetworkService:NodeNetworkService, private authService:AuthService, 
      public mainLayout:MainLayoutService, private modalService:NzModalService
   ) 
   { }


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

         },

         errorResponse => {
            console.log(errorResponse);
         }

      );
   }


   getUserNetworks():void 
   {
      this.nodeNetworkService.getUserNetworks().subscribe(

         (response:NetworkInfo) => {

            this.networkInfo = response;
            console.log(response);
            
         },

         error => {
            console.log(error);
         }

      );
   }


   showNetworkGenerationConfirm():void
   {
      this.modalService.confirm({
         
         nzTitle  : '<i>Create a new network?</i>',
         nzContent: '<b>A network will not be active unless it has more than two nodes</b>',
         nzOkText: 'Create',

         nzOnOk   : () => { 
            this.onNetworkGenerationSubmit();
         }
         
      });
   }


   onNetworkGenerationSubmit():void
   {
      let user:UserInfo = this.authService.getCurrentUser();
      console.log(user);

      // TODO: Send a network creation request

      // TODO: Add this network to current user networks

      // TODO: Save network chain to local DB
   }  
}
