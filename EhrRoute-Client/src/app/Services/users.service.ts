import { BlockAdditionRequest } from './../Models/Payload/Requests/BlockAdditionRequest';
import { first, catchError } from 'rxjs/operators';
import { RoleChangeRequest } from './../Models/Payload/Requests/RoleChangeRequest';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment.prod';
import { Observable, throwError } from 'rxjs';


@Injectable({
  providedIn: 'root'
})


export class UsersService 
{
   
   private userRoleChangeUrl:string = environment.apiUrl + '/auth/user-role-change';
   private userSearchUrl:string = environment.apiUrl + '/users/search-by-username';
   private getUserInfoUrl:string = environment.apiUrl + '/users/get-by-username/';
   private getUserEhrConsentUrl:string = environment.apiUrl + '/transaction/getConsent';
   private userFirstLoginStatusUrl:string = environment.apiUrl + '/users/current/first-login-status';


   constructor(private http:HttpClient) {
   }


   public changeUserRole(roleChange:RoleChangeRequest): Observable<any> {

      return this.http.post(this.userRoleChangeUrl, roleChange).pipe(first(),

         catchError(error => {
            return throwError(error);
         })

      );

   }


   public searchUsername(username:string): Observable<any> {
      
      let searchUrl = this.userSearchUrl + "?keyword=" + username;

      return this.http.get(searchUrl).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })

      );
   }


   public getUserInfo(username:string): Observable<any> {

      let userGetUrl = this.getUserInfoUrl + username;

      return this.http.get(userGetUrl).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })

      );

   }


   public getCurrentUserFirstLoginStatus(): Observable<any>
   {
      return this.http.get(this.userFirstLoginStatusUrl).pipe(first(),

         catchError(error => {
            return throwError(error);
         })

      );
   }


   public sendUserEhrConsentRequest(blockAdditionRequest:BlockAdditionRequest): Observable<any>
   {
      return this.http.post(this.getUserEhrConsentUrl, blockAdditionRequest).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })

      );
   }

}
