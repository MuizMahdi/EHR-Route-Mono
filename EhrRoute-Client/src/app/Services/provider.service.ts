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


   public getCurrentProviderUUID(): Observable<any> {

      return this.http.get(this.currentProviderUuidUrl).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })

      );

   }


}