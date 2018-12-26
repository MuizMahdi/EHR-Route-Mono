import { MainLayoutService } from './Services/main-layout.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { NodeClustersService } from './Services/node-clusters.service';

@Component({
   selector: 'app-root',
   templateUrl: './app.component.html',
   styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit, OnDestroy
{
   constructor(public mainLayout:MainLayoutService, private clustersService:NodeClustersService) { }


   ngOnInit() {
      //this.mainLayout.hide();

      // Make sure that user isn't subscribed to any cluster before subscribing
      this.clustersService.unsubscribeClusters();

      // Subscribe to providers and consumers cluster
      this.clustersService.subscribeProvider();
      this.clustersService.subscribeConsumer();

      // Call ngOnDestroy before refreshing page if the user refreshes it
      window.onbeforeunload = () => this.ngOnDestroy();
      
   }


   ngOnDestroy() {
      console.log("[[[[[[[[ NG ON DESTROY ]]]]]]]]");
      // Unsubscribe for all clusters
      this.clustersService.unsubscribeClusters();
   }
}
