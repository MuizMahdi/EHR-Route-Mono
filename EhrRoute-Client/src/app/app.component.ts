import { MainLayoutService } from './Services/main-layout.service';
import { Component, OnInit } from '@angular/core';

@Component({
   selector: 'app-root',
   templateUrl: './app.component.html',
   styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit 
{
   constructor(public mainLayout:MainLayoutService) { }

   ngOnInit() {
      //this.mainLayout.hide();
   }
}
