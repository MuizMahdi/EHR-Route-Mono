import { NodeNetworkService } from 'src/app/Services/node-network.service';
import { Component, OnInit } from '@angular/core';
import { MainLayoutService } from './Services/main-layout.service';


@Component({
   selector: 'app-root',
   templateUrl: './app.component.html',
   styleUrls: ['./app.component.css']
})


export class AppComponent implements OnInit
{
   constructor(public mainLayout:MainLayoutService, private networkService:NodeNetworkService) 
   { }


   ngOnInit()
   {
      // Establish connections to all user networks DBs
      this.networkService.checkUserNetworks();
   }

}
