import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { catchError, first } from 'rxjs/operators';
import { Notification } from '../Models/Payload/Responses/Notification';


@Injectable({
  providedIn: 'root'
})


export class NotificationService 
{
   notificationsGetUrl:string = environment.apiUrl + "/notifications/current-user";
   notificationUrl:string = environment.apiUrl + "/notifications/";

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


   deleteNotification(notificationID:number): Observable<any>
   {
      return this.http.delete(this.notificationUrl + notificationID).pipe(first(),
      
         catchError(error => {
            return throwError(error);
         })

      );
   }

}
