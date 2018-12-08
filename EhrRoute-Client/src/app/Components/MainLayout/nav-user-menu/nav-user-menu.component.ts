import { AuthService } from './../../../Services/auth.service';
import { Component} from '@angular/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-nav-user-menu',
  templateUrl: './nav-user-menu.component.html',
  styleUrls: ['./nav-user-menu.component.css']
})


export class NavUserMenuComponent 
{
   navUserMenu: boolean = false;

   constructor(private authService:AuthService, private router:Router) 
   { }

   logout():void
   {
      this.authService.logout();
      this.router.navigate(['login']);
   }

}
