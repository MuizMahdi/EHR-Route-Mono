import { HttpRequest, HttpInterceptor, HttpEvent, HttpHandler } from '@angular/common/http';
import { AuthService } from './../../Services/auth.service';
import { Injectable } from "@angular/core";
import { Observable } from 'rxjs';



@Injectable()
export class JwtInterceptor implements HttpInterceptor
{
   constructor(private authService: AuthService)
   { }

   intercept(request: HttpRequest<any>, handler: HttpHandler): Observable<HttpEvent<any>>
   {
      // Add Authorization header with the Jwt to all request if it is available
      let Jwt = localStorage.getItem('accessToken');

      if (Jwt) 
      {
         // Clone the request and set its authorization header with the token
         request = request.clone({
            setHeaders: {
               Authorization: "Bearer " + Jwt
            }
         });
      }

      // Handle the request and move into next interceptors if available
      return handler.handle(request); 
   }
}