import { ChainService } from './../../../Services/chain.service';
import { ElectronicHealthRecord } from './../../../Models/App/ElectronicHealthRecord';
import { NodeNetworkService } from 'src/app/Services/node-network.service';
import { EhrService } from './../../../Services/ehr.service';
import { Component, OnInit } from '@angular/core';
import { MainLayoutService } from 'src/app/Services/main-layout.service';


@Component({
  selector: 'app-health-records-manager',
  templateUrl: './health-records-manager.component.html',
  styleUrls: ['./health-records-manager.component.css']
})


export class HealthRecordsManagerComponent implements OnInit 
{
   pageSize: number = 3;
   pageNumber: number = 1;
   totalPagesNumber: number;
   records: ElectronicHealthRecord[] = [];
   userNetworksUUIDs: string[] = [];
   userHasNetworks: boolean = true;


   constructor (
      public mainLayout:MainLayoutService, private ehrService:EhrService,
      private networkService:NodeNetworkService, private chainService:ChainService
   ) { 
      mainLayout.show();
   }


   async ngOnInit() {

      this.mainLayout.show();

      // Get user networks UUIDs
      await this.getUserNetworksUUIDs();

      // Then get the medical records
      this.getRecords();

   }


   private async getUserNetworksUUIDs()
   {
      // TODO: Show a spining bar while getting user networks and records
      await this.networkService.getUserNetworksUUIDs().then(async networksUUIDs => {

         this.userNetworksUUIDs = networksUUIDs;

         // Get the count of all the available records to setup the number of pages
         await this.chainService.countAllNetworksBlocks(networksUUIDs).then(count => {
            this.totalPagesNumber = Math.ceil(count / this.pageSize) * 10;
         });

      }).catch(error => {
         this.userHasNetworks = false;
      });
   }


   private getRecords(): void
   {
      if (this.userHasNetworks) {

         // Get the medical records
         this.ehrService.getNetworksRecords(
            this.userNetworksUUIDs,
            this.pageNumber,
            this.pageSize
         ).then(records => {
            this.records = records;
         });

      }
   }


   private changePageNumber(pageNumber:number): void {
      this.pageNumber = pageNumber;
      this.getRecords();
   }


   private changePageSize(pageSize:number): void {
      this.pageSize = pageSize;
      this.getRecords();
   }

}
