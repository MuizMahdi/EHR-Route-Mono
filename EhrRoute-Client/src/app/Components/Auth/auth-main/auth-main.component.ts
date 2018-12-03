import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-auth-main',
  templateUrl: './auth-main.component.html',
  styleUrls: ['./auth-main.component.css']
})


export class AuthMainComponent implements OnInit 
{
   isOnRegister:boolean;

   constructor() 
   { }

   ngOnInit() 
   {
      this.isOnRegister = false;
   }

}
