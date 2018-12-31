import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-nav-user-notifications',
  templateUrl: './nav-user-notifications.component.html',
  styleUrls: ['./nav-user-notifications.component.css']
})


export class NavUserNotificationsComponent implements OnInit 
{
   navNotificationsMenu: boolean = false;

   constructor() { }
   ngOnInit() { }


   onClickedOutside(e: Event) 
   {
      this.navNotificationsMenu = false;
   }

}
