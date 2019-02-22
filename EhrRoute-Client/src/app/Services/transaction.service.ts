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
   private giveUserEhrConsentUrl:string = environment.apiUrl + '/transaction/give-consent';


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
      console.log("SENDING USER CONSENT RESPONSE");

      return this.http.post(this.giveUserEhrConsentUrl, userConsentResponse).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })
      
      );
   }
}
