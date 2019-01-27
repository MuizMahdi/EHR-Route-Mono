import { ErrorResponse } from 'src/app/Models/Payload/Responses/ErrorResponse';
import { UserInfo } from './../../../Models/Payload/Responses/UserInfo';
import { UsersService } from './../../../Services/users.service';
import { Component, OnInit, Input } from '@angular/core';


@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})


export class UserProfileComponent implements OnInit
{
   // Searched username
   @Input() searchParameter: string;

   searchedUser:UserInfo = null;


   constructor(private userService:UsersService) 
   { }


   ngOnInit()
   {
      if (this.searchParameter) {

         // Get user profile
         this.userService.getUserInfo(this.searchParameter).subscribe(

            (response:UserInfo) => {
               this.searchedUser = response;
            },

            (error:ErrorResponse) => {
               console.log(error);
            }

         );
      }
   }


}
