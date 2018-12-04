import { environment } from './../../environments/environment';
import { UserLoginRequest } from './../Models/UserLoginRequest';
import { UserRegistrationRequest } from './../Models/UserRegistrationRequest';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap, shareReplay, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';


@Injectable({
  providedIn: 'root'
})


export class AuthService
{
   registrationUrl:string = environment.apiUrl + '/auth/signup';
   loginUrl:string = environment.apiUrl + '/auth/signin';


   constructor(private http:HttpClient) 
   { }


   register(userInfo: UserRegistrationRequest) 
   {
      return this.http.post(this.registrationUrl, userInfo).pipe(

         catchError(error => { // Catch error
            return throwError(error); // Rethrow the error
         })
         
      );
   }


   login(userInfo: UserLoginRequest)
   {
      return this.http.post(this.loginUrl, userInfo).pipe(

         tap(tokenResponse => {

            this.saveJwtSession(tokenResponse),
            shareReplay()

         }),
         
         catchError(error => { // Catch error
            return throwError(error); // Rethrow the error
         })

      );
   }

   
   // Removes user from local storage
   logout()
   {
      localStorage.removeItem('accessToken');
   }


   // Saves jwt to local storage
   saveJwtSession(jwtToken)
   {
      if(jwtToken && jwtToken.accessToken)
      {
         localStorage.setItem('accessToken', jwtToken.accessToken)
      }
   }

}
