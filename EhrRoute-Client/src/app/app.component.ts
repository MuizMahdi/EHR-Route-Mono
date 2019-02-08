import { Component } from '@angular/core';
import { MainLayoutService } from './Services/main-layout.service';


@Component({
   selector: 'app-root',
   templateUrl: './app.component.html',
   styleUrls: ['./app.component.css']
})


export class AppComponent
{
   constructor(public mainLayout:MainLayoutService) 
   { }
}
