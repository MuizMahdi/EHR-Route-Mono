import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './../Services/auth.service';
import { Observable } from 'rxjs';
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
      
      const currentUser = this.authService.getCurrentUser;

      if (currentUser) {
         // Return true if user is logged in
         return true;
      }

      // Navigate user to login page if not logged in
      this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });

      return false;
      
   }
}