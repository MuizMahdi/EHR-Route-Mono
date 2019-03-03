import { UserUpdateConsentResponse } from './../Models/Payload/Responses/UserUpdateConsentResponse';
import { UpdatedBlockAdditionRequest } from './../Models/Payload/Requests/UpdatedBlockAdditionRequest';
import { UserConsentResponse } from './../Models/Payload/Requests/UserConsentResponse';
import { first, catchError } from 'rxjs/operators';
import { environment } from './../../environments/environment.prod';
import { Observable, throwError } from 'rxjs';
import { BlockAdditionRequest } from './../Models/Payload/Requests/BlockAdditionRequest';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
})


export class TransactionService 
{
   private getUserEhrConsentUrl:string = environment.apiUrl + '/transaction/getConsent';
   private getEhrUpdateConsentUrl:string = environment.apiUrl + '/transaction/get-update-consent';
   private giveUserEhrConsentUrl:string = environment.apiUrl + '/transaction/give-consent';
   private giveEhrUpdateConsentUrl:string = environment.apiUrl + '/transaction/give-update-consent';


   constructor(private http:HttpClient) 
   { }


   public sendUserEhrConsentRequest(blockAdditionRequest:BlockAdditionRequest): Observable<any>
   {
      return this.http.post(this.getUserEhrConsentUrl, blockAdditionRequest).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })

      );
   }


   public sendUserEhrConsentResponse(userConsentResponse:UserConsentResponse): Observable<any>
   {
      return this.http.post(this.giveUserEhrConsentUrl, userConsentResponse).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })
      
      );
   }


   public sendEhrUpdateConsentResponse(updatedBlockRequest:UpdatedBlockAdditionRequest): Observable<any>
   {
      return this.http.post(this.getEhrUpdateConsentUrl, updatedBlockRequest).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })
   
      );
   }


   public sendUpdateEhrConsentResponse(updateConsentResponse:UserUpdateConsentResponse): Observable<any>
   {
      return this.http.post(this.giveEhrUpdateConsentUrl, updateConsentResponse).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })
   
      );
   }
}
