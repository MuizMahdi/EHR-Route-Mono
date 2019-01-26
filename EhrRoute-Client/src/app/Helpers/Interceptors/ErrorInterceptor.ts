import { AuthService } from './../../Services/auth.service';
import { Injectable } from "@angular/core";
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from "@angular/common/http";
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ErrorResponse } from 'src/app/Models/Payload/Responses/ErrorResponse';


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

            // Logout if http status code 401 response is returned
            if(error.status === 401) {
               this.authService.logout();
               location.reload(true);
            }

            const errorResponse:ErrorResponse = {
               httpStatus: error.status,
               success: error.error.success,
               message: error.error.message
            };

            return throwError(errorResponse);
            
         })
      )
   }


   // For Development, logs error info.
   private handleError(error:HttpErrorResponse)
   {
      console.log("ApiResponse MESSAGE: " + error.error.message); // ApiResponse Message
      console.log("ApiResponse SUCCESS: " + error.error.success); // ApiResponse Success
      console.log("error STATUS: " + error.status); // 404 (NOT_FOUND)
      console.log("error OK: " + error.ok); // False (not OK)
   }
}
