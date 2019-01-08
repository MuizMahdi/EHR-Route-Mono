import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment.prod';
import { catchError, first } from 'rxjs/operators';
import { throwError, Observable } from 'rxjs';
import { NetworkInvitationRequest } from '../Models/Payload/Requests/NetworkInvitationRequest';


@Injectable({
  providedIn: 'root'
})


export class NodeNetworkService
{
   
   userNetworkUrl:string = environment.apiUrl + '/users/current/networks';
   networkRootUrl:string = environment.apiUrl + '/network/get-root';
   createNetworkUrl:string = environment.apiUrl + '/network/create';
   networkInviteUrl:string = environment.apiUrl + '/network/invite';

   
   constructor(private http:HttpClient) { }


   getUserNetworks(): Observable<any> {
      
      return this.http.get(this.userNetworkUrl).pipe(first(),

         catchError(error => {
            return throwError(error);
         })
         
      );
      
   }


   getNetworkRoot(networkUUID:string): Observable<any> {

      return this.http.get(this.networkRootUrl + '?networkuuid=' + networkUUID).pipe(first(),

         catchError(error => {
            return throwError(error);
         })

      );

   }

   
   generateNetwork(networkName:string): Observable<any> {

      return this.http.post(this.createNetworkUrl, networkName).pipe(first(),
         
         catchError(error => {
            return throwError(error);
         })

      );

   }


   sendNetworkInvitationRequest(invitationRequest:NetworkInvitationRequest): Observable<any> {

      return this.http.post(this.networkInviteUrl, invitationRequest).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })

      );

   }
}
