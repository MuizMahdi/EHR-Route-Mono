import { UserLoginRequest } from './../../../Models/UserLoginRequest';
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

   constructor(private router: Router, private authService: AuthService, private electron: ElectronService) {
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
            // TODO: Navigate to main page after login
            this.router.navigate(['']);
         },
         
         errorResponse => {
            // TODO: Handle error by showing a flash message to user
            console.log(errorResponse);
         }

      );
   }


   

}
