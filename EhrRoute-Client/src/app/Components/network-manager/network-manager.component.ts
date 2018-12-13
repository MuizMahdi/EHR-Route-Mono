import { MainLayoutService } from './../../Services/main-layout.service';
import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-network-manager',
  templateUrl: './network-manager.component.html',
  styleUrls: ['./network-manager.component.css']
})


export class NetworkManagerComponent implements OnInit 
{

   constructor(public mainLayout:MainLayoutService) { }

   ngOnInit() {
      this.mainLayout.show();
   }

}
