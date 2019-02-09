import { AddressResponse } from './../../../Models/Payload/Responses/AddressResponse';
import { AddressService } from './../../../Services/address.service';
import { ErrorResponse } from './../../../Models/Payload/Responses/ErrorResponse';
import { UsersService } from './../../../Services/users.service';
import { UserLoginRequest } from '../../../Models/Payload/Requests/UserLoginRequest';
import { AuthService } from './../../../Services/auth.service';
import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { first, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { ElectronService } from 'ngx-electron';
import { UserRole } from 'src/app/Models/UserRole';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})


export class LoginComponent
{
   loginFormGroup: FormGroup;

   loginUsernameOrEmail: string;
   loginPassword: string;


   constructor(
      private router:Router, private authService:AuthService, 
      private electron:ElectronService, private userService:UsersService,
      private addressService:AddressService
   ) {
      this.buildForm();
   }


   buildForm(): void
   {
      this.loginFormGroup = new FormGroup({
         usernameOrEmailCtrl: new FormControl(null, [Validators.required]),
         passwordCtrl: new FormControl(null, Validators.required)
      });
   }


   onLogin()
   {
      // LoginFormGroup values
      this.loginUsernameOrEmail = this.loginFormGroup.get("usernameOrEmailCtrl").value;
      this.loginPassword = this.loginFormGroup.get("passwordCtrl").value;

      const userInfo: UserLoginRequest = {
         usernameOrEmail: this.loginUsernameOrEmail,
         password: this.loginPassword
      };

      this.authService.login(userInfo)
      .pipe( // Login and get response

         first(), // Get first value in stream (The Token)

         catchError(response => { // In case an error occurs
            return throwError(response) // Re-Throw the error to be handled on subscription
         })

      ).subscribe(

         response => {
            this.router.navigate(['main']);
            this.checkIfFirstLogin();
            this.checkIfIsProvider();
         },
         
         (error:ErrorResponse) => {
            // TODO: Handle error by showing a flash message to user
            console.log(error);
         }

      );
   }


   checkIfFirstLogin(): void
   {
      this.userService.getCurrentUserFirstLoginStatus().subscribe(

         (isFirstLogin:boolean) => {
            // If its the user's first time login, then request for address generation
            if (isFirstLogin)
            {
               // Generate an address for the user to be saved on local DB
               this.addressService.generateUserAddress().subscribe(

                  (addressResponse:AddressResponse) => {
                     // Get current user id
                     let userID: number = this.authService.getCurrentUser().id;
                     // Persist address locally
                     this.addressService.saveUserAddress(addressResponse, userID);
                  },

                  (error:ErrorResponse) => {
                     console.log(error);
                  }

               );
            }

         },

         (error:ErrorResponse) => {
            console.log(error);
         }

      );
   }


   checkIfIsProvider()
   {
      // Get user roles
      this.authService.getCurrentUserRoles().subscribe(
         
         (response:UserRole[]) => {

            // Go through roles and check if user has a Provider role
            response.forEach((role:UserRole) => {
               
               if (role.roleName.trim() === 'ROLE_PROVIDER') 
               {
                  // Check if user has their provider details saved

                  // If not, then send the provider's address to be saved
               }

            });

         },

         errorResponse => {
            console.log(errorResponse);
         }
      );
   }

}
