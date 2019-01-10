import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { catchError } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})


export class NotificationService 
{
   notificationsGetUrl:string = environment.apiUrl + "/notifications/current-user"

   constructor(private http:HttpClient) 
   { }


   getUserNotifications(): Observable<any>
   {
      return this.http.get(this.notificationsGetUrl).pipe(

         catchError(error => {
            return throwError(error);
         })

      );
   }

}
