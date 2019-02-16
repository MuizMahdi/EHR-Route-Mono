import { ProviderAdditionRequest } from './../Models/Payload/Requests/ProviderAdditionRequest';
import { SimpleStringPayload } from './../Models/Payload/Responses/SimpleStringPayload';
import { first, catchError } from 'rxjs/operators';
import { Observable, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
})


export class ProviderService 
{

   private providerSearchUrl:string = environment.apiUrl + '/providers/search-providers-by-username';
   private currentProviderUuidUrl:string = environment.apiUrl + '/providers/current/uuid';
   private checkProviderAddressExistenceUrl:string = environment.apiUrl + '/providers/current/address/exists';
   private providerAddressUrl:string = environment.apiUrl + '/providers/current/address';
   private registerInstitutionProviderUrl:string = environment.apiUrl + '/users/providers'


   constructor(private http:HttpClient) 
   { }


   public searchProviderUsername(username:string): Observable<any> {
      
      let searchUrl = this.providerSearchUrl + "?keyword=" + username;

      return this.http.get(searchUrl).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })

      );

   }


   public async getCurrentProviderUUID(): Promise<any> {

      return await this.http.get(this.currentProviderUuidUrl).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })

      ).toPromise();

   }


   public checkProviderAddressExistence(): Observable<any> {
      
      return this.http.get(this.checkProviderAddressExistenceUrl).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })
      
      );

   }


   public saveProviderAddress(address:string): Observable<any> {

      let addressPayload:SimpleStringPayload = {
         payload: address
      };

      return this.http.post(this.providerAddressUrl, addressPayload).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })
      
      );

   }


   public registerInstitutionProvider(providerAdditionRequest:ProviderAdditionRequest) {

      return this.http.post(this.registerInstitutionProviderUrl, providerAdditionRequest).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })
      
      );

   }

}
