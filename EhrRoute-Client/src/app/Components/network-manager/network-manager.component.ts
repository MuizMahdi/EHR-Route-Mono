import { UserRole } from './../../Models/UserRole';
import { MainLayoutService } from './../../Services/main-layout.service';
import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/Services/auth.service';
import { tap, first } from 'rxjs/operators';


@Component({
  selector: 'app-network-manager',
  templateUrl: './network-manager.component.html',
  styleUrls: ['./network-manager.component.css']
})


export class NetworkManagerComponent implements OnInit 
{
   isAdmin:boolean = false;
   isProvider:boolean = false;


   constructor(private authService:AuthService, public mainLayout:MainLayoutService) { }


   ngOnInit() {
      this.mainLayout.show();
      this.initUserRole();
   }


   initUserRole() {
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

}
