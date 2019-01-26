import { UserInfo } from '../Models/Payload/Responses/UserInfo';
import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './../Services/auth.service';
import { Observable, Subject } from 'rxjs';
import { first } from 'rxjs/operators';


@Injectable({
   providedIn:'root'
})


export class AuthGuard implements CanActivate
{
   constructor(private router:Router, private authService:AuthService)
   { }

   canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot)
   {
      const accessToken = this.authService.getAccessToken();

      if (accessToken) {
         // Return true if user is logged in
         return true;
      }

      // Navigate user to login page if not logged in
      this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });

      return false;
   }
}