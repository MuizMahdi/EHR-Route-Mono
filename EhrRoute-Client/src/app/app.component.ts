import { MainLayoutService } from './Services/main-layout.service';
import { Component, OnInit } from '@angular/core';
import { NodeClustersService } from './Services/node-clusters.service';

@Component({
   selector: 'app-root',
   templateUrl: './app.component.html',
   styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit 
{
   constructor(public mainLayout:MainLayoutService, private clustersService:NodeClustersService) { }

   ngOnInit() {
      //this.mainLayout.hide();
      this.clustersService.subscribeProvider();
      this.clustersService.subscribeConsumer();
   }
}
