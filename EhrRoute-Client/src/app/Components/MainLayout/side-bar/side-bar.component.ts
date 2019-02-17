import { AuthService } from './../../../Services/auth.service';
import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.css']
})


export class SideBarComponent implements OnInit 
{
   isUserProvider: boolean = false;


   constructor(private authService:AuthService) { }


   ngOnInit() {
      if (this.authService.isUserProvider()) {
         this.isUserProvider = true;
      }
   }
}
