import { UserInfo } from '../Models/Payload/Responses/UserInfo';
import { environment } from './../../environments/environment';
import { UserLoginRequest } from '../Models/Payload/Requests/UserLoginRequest';
import { UserRegistrationRequest } from '../Models/Payload/Requests/UserRegistrationRequest';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap, shareReplay, catchError, first } from 'rxjs/operators';
import { throwError, Subject, Observable } from 'rxjs';
import { NodeClustersService } from './node-clusters.service';


@Injectable({
  providedIn: 'root'
})


export class AuthService
{
   registrationUrl:string = environment.apiUrl + '/auth/signup';
   loginUrl:string = environment.apiUrl + '/auth/signin';
   getCurrentUserUrl:string = environment.apiUrl + '/users/current';
   userRolesUrl: string = environment.apiUrl + '/users/current/roles';
   

   currentUser: Subject<UserInfo> = new Subject<UserInfo>();
   isLoggedIn:boolean = false;


   constructor(private http:HttpClient, private clustersService:NodeClustersService) 
   { }


   public register(userRegistrationInfo: UserRegistrationRequest): Observable<any>
   {
      return this.http.post(this.registrationUrl, userRegistrationInfo).pipe(

         catchError(error => { // Catch error
            return throwError(error); // Rethrow the error
         })
         
      );
   }


   public login(userLoginInfo: UserLoginRequest): Observable<any>
   {

      return this.http.post(this.loginUrl, userLoginInfo).pipe(

         tap(tokenResponse => {

            this.saveSession(tokenResponse);
            this.isLoggedIn = true;

            // Subscribe user node to providers and consumers cluster
            this.clustersService.subscribeProvider();
            this.clustersService.subscribeConsumer();
            
            shareReplay()

         }),
         
         catchError(error => { // Catch error
            return throwError(error); // Rethrow the error
         })

      );
   }


   public getCurrentUser(): UserInfo
   {
      // Get user info from local storage
      return JSON.parse(localStorage.getItem('currentUser')) as UserInfo;
   }


   public getAccessToken():any
   {
      return localStorage.getItem('accessToken');
   }

   
   public getCurrentUserRoles(): Observable<any>
   {
      return this.http.get(this.userRolesUrl).pipe(first(),
         catchError(error => {
            return throwError(error);
         })
      );
   }


   public getCurrentUserInfo(): Observable<any>
   {
      return this.http.get(this.getCurrentUserUrl).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })

      );
   }


   public logout(): void
   {
      // Unsubscribe user node from clusters
      this.clustersService.unsubscribeClusters();

      // Remove user token
      localStorage.removeItem('accessToken');

      // Remove user info
      localStorage.removeItem('currentUser');

      // Reset currentUser subject
      this.currentUser.next(null);
   }


   private saveSession(jwtToken): void
   {
      if(jwtToken && jwtToken.accessToken)
      {
         // Save jwt to local storage
         localStorage.setItem('accessToken', jwtToken.accessToken)
      }

      this.saveCurrentUserInfo();
   }


   private saveCurrentUserInfo(): void
   {
      this.http.get(this.getCurrentUserUrl).pipe(first()).subscribe(
         
         (user:UserInfo) => {
         
            if(user) {
               // Save the user info in local storage
               localStorage.setItem('currentUser', JSON.stringify(user))

               // Then set the user in the user subject
               this.currentUser.next(user);
            }

         },

         error => {
            console.log(error);
         }
         
      );
   }

}
