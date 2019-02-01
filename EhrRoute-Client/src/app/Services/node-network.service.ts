import { UserInfo } from './../Models/Payload/Responses/UserInfo';
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
   
   private userNetworkUrl:string = environment.apiUrl + '/users/current/networks';
   private networkRootUrl:string = environment.apiUrl + '/network/get-root';
   private createNetworkUrl:string = environment.apiUrl + '/network/create';
   private networkInviteUrl:string = environment.apiUrl + '/network/invite';
   private networkInvitationAcceptUrl:string = environment.apiUrl + '/network/invitation-accept';
   private searchNetworksByNameUrl:string = environment.apiUrl + '/network/search-by-name';
   private getNetworkUuidByNameUrl:string = environment.apiUrl + '/network/uuid'

   
   constructor(private http:HttpClient) { }


   public getUserNetworks(): Observable<any> {
      
      return this.http.get(this.userNetworkUrl).pipe(first(),

         catchError(error => {
            return throwError(error);
         })
         
      );
      
   }


   public getNetworkRoot(networkUUID:string): Observable<any> {

      return this.http.get(this.networkRootUrl + '?networkuuid=' + networkUUID).pipe(first(),

         catchError(error => {
            return throwError(error);
         })

      );

   }


   public getNetworkUuidByName(networkName:string): Observable<any> {

      let url = this.getNetworkUuidByNameUrl + "?name=" + networkName;

      return this.http.get(url).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })

      );

   }


   public searchNetworksByName(networkName:string): Observable<any> {

      let searchUrl = this.searchNetworksByNameUrl + "?keyword=" + networkName;

      return this.http.get(searchUrl).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })

      );

   }

   
   public generateNetwork(networkName:string): Observable<any> {

      return this.http.post(this.createNetworkUrl, networkName).pipe(first(),
         
         catchError(error => {
            return throwError(error);
         })

      );

   }


   public sendNetworkInvitationRequest(invitationRequest:NetworkInvitationRequest): Observable<any> {

      return this.http.post(this.networkInviteUrl, invitationRequest).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })

      );

   }


   public networkInvitationAccept(invitationResponse:NetworkInvitationRequest): Observable<any> {

      return this.http.post(this.networkInvitationAcceptUrl, invitationResponse).pipe(first(),

         catchError(error => {
            return throwError(error);
         })

      );

   }
}
