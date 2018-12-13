import { MainLayoutService } from './../../../Services/main-layout.service';
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

   constructor(private authService:AuthService, private router:Router, public mainLayout:MainLayoutService) 
   { }

   logout():void
   {
      this.authService.logout();
      this.mainLayout.hide();
      this.router.navigate(['login']);
   }

   onClickedOutside(e: Event) 
   {
      this.navUserMenu = false;
   }

}
