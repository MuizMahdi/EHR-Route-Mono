import { ErrorResponse } from 'src/app/Models/Payload/Responses/ErrorResponse';
import { ProviderAdditionRequest } from './../../../Models/Payload/Requests/ProviderAdditionRequest';
import { ProviderService } from './../../../Services/provider.service';
import { UsersService } from './../../../Services/users.service';
import { RoleChangeRequest } from './../../../Models/Payload/Requests/RoleChangeRequest';
import { MainLayoutService } from './../../../Services/main-layout.service';
import { Component } from '@angular/core';


@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})


export class AdminPanelComponent
{
   roleChangeUsername:string;
   selectedRoleChangeValue:string = "ROLE_PROVIDER"

   institutionProviderUsername:string;
   institutionName:string;


   constructor(
      private userService:UsersService, public mainLayout:MainLayoutService,
      private providerService:ProviderService
   ) { 
      this.mainLayout.show(); 
   }


   changeUserRole(username:string, role:string) {

      let roleChangeReq:RoleChangeRequest = {
         username,
         role
      }

      this.userService.changeUserRole(roleChangeReq).subscribe(

         response => {
            console.log(response);
         },

         error => {
            console.log(error);
         }

      );

   }


   registerInstitutionProvider(providerUsername:string, institutionName:string) {

      let request:ProviderAdditionRequest = {
         username: providerUsername,
         institutionName: institutionName
      }

      this.providerService.registerInstitutionProvider(request).subscribe(

         response => {
            console.log(response);
         },

         (error:ErrorResponse) => {
            console.log(error);
         }

      );

   }
}
