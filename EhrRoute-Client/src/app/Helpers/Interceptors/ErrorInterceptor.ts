import { AuthService } from './../../Services/auth.service';
import { Injectable } from "@angular/core";
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from "@angular/common/http";
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';


@Injectable()
export class ErrorInterceptor implements HttpInterceptor
{
   constructor(private authService:AuthService)
   { }

   intercept(request:HttpRequest<any>, handler:HttpHandler): Observable<HttpEvent<any>>
   {
      return handler.handle(request).pipe(

         // Catch Http errors
         catchError(error => {

            if(error.status === 401) {
               // Logout if http status code 401 response is returned
               this.authService.logout;
               location.reload(true);
            }

            const errorMessage = error.error.message || error.statusText;

            return throwError(errorMessage);
         })
      )
   }
}
