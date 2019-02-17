import { UserInfo } from './../../../Models/Payload/Responses/UserInfo';
import { RoleName } from './../../../Models/RoleName';
import { AuthService } from './../../../Services/auth.service';
import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css']
})


export class SideBarComponent implements OnInit 
{
   isUserProviderOrAdmin: boolean = false;


   constructor(private authService:AuthService) { }


   ngOnInit() {
      this.checkUserRoles();
   }


   private checkUserRoles(): void
   {
      this.authService.currentUser.subscribe((userInfo:UserInfo) => {

         userInfo.roles.forEach(role => {

            // If user has a provider or an admin role
            if (role === RoleName.PROVIDER || role === RoleName.ADMIN) {
               this.isUserProviderOrAdmin = true;
            }

         });
         
      });
   }
}
