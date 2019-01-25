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


   constructor(private userService:UsersService, public mainLayout:MainLayoutService) { 
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
}
