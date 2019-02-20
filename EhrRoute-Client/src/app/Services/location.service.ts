import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { Injectable } from '@angular/core';
import { first, catchError } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})


export class LocationService 
{
   constructor(private http:HttpClient) 
   { }


   public getCountries(): Observable<any>
   {
      return this.http.get("https://api.teleport.org/api/countries/").pipe(first(),
     
         catchError(error => {
            return throwError(error);
         })
     
      );
   }


   public getCities(countryName:string): Observable<any>
   {
      let url = "https://api.teleport.org/api/cities/?search=" + countryName + "&embed=city%3Asearch-results%2Fcity%3Aitem%2Fcity";
      
      return this.http.get(url).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })
      
      );
   }
}
