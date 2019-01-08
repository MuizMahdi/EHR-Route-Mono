import { first, catchError } from 'rxjs/operators';
import { RoleChangeRequest } from './../Models/Payload/Requests/RoleChangeRequest';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment.prod';
import { Observable, throwError } from 'rxjs';


@Injectable({
  providedIn: 'root'
})


export class UsersService 
{
   
   userRoleChangeUrl:string = environment.apiUrl + '/auth/user-role-change';


   constructor(private http:HttpClient) { }


   changeUserRole(roleChange:RoleChangeRequest): Observable<any> {

      return this.http.post(this.userRoleChangeUrl, roleChange).pipe(first(),

         catchError(error => {
            return throwError(error);
         })

      );

   }

}
