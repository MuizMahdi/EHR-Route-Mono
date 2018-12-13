import { MainLayoutService } from './../../../Services/main-layout.service';
import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})


export class MainComponent implements OnInit
{
   constructor(public mainLayout:MainLayoutService) { }

   ngOnInit() {
      this.mainLayout.show();
   }

}
