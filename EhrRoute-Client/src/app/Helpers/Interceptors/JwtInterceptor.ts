import { HttpEvent } from '@angular/common/http';
import { HttpHandler } from '@angular/common/http';
import { HttpRequest } from '@angular/common/http';
import { AuthService } from './../../Services/auth.service';
import { HttpInterceptor } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { Observable } from 'rxjs';


@Injectable()
export class JwtInterceptor implements HttpInterceptor
{
   constructor(private authService: AuthService)
   { }

   intercept(request:HttpRequest<any>, handler:HttpHandler): Observable<HttpEvent<any>>
   {
      // Add Authorization header with the Jwt to all request if it is available
      let currentUser = this.authService.getCurrentUser;
      let Jwt = localStorage.getItem('accessToken');

      if (currentUser && Jwt) 
      {
         // Clone the request and set its authorization header with the token
         request = request.clone({
            setHeaders: {
               Authorization: 'Bearer ${Jwt}'
            }
         });
      }

      // Handle the request and move into next interceptors if available
      return handler.handle(request); 
   }
}