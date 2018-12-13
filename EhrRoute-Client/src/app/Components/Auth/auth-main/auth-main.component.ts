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
      console.log("AUTH COMPONENT");
      this.mainLayout.hide();
      this.isOnRegister = false;
   }

}
