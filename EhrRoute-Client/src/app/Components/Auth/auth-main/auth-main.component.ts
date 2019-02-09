import { MainLayoutService } from './../../../Services/main-layout.service';
import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-auth-main',
  templateUrl: './auth-main.component.html',
  styleUrls: ['./auth-main.component.css']
})


export class AuthMainComponent implements OnInit 
{
   isOnRegister:boolean;

   constructor(public mainLayout:MainLayoutService) 
   { }

   ngOnInit() 
   {
      // Handles when user reloads auth page, disabling the prompt on login page,
      // which is only shown when user reloads page after logging in.
      //this.handleOnAuthPageReloads();

      this.mainLayout.hide();
      this.isOnRegister = false;
   }


   handleOnAuthPageReloads(): void
   {
      window.onbeforeunload = function(evt) {       
         evt.returnValue = '';
         return '';
      }
     
      window.onunload = function () { }
   }

}
