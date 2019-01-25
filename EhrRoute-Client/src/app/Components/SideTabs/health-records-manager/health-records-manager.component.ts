import { Component, OnInit } from '@angular/core';
import { MainLayoutService } from 'src/app/Services/main-layout.service';


@Component({
  selector: 'app-health-records-manager',
  templateUrl: './health-records-manager.component.html',
  styleUrls: ['./health-records-manager.component.css']
})


export class HealthRecordsManagerComponent implements OnInit 
{

   constructor(public mainLayout:MainLayoutService) 
   { 
      mainLayout.show();
   }


   ngOnInit() 
   {
      this.mainLayout.show();
   }

}
