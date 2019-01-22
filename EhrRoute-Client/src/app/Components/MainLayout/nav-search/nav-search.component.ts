import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-nav-search',
  templateUrl: './nav-search.component.html',
  styleUrls: ['./nav-search.component.css']
})


export class NavSearchComponent implements OnInit 
{

   // "Network" option is initially selected
   selectedSearchOption:string = "Network"; 
   searchInputValue:string = "";
   searchOptions = [];


   constructor() 
   { }


   ngOnInit() 
   { }

   
   onSearch()
   {
      console.log(this.selectedSearchOption);
      console.log(this.searchInputValue);
   }


   onInput(value: string): void {
      this.searchOptions = value ? [
        value,
        value + value,
        value + value + value
      ] : [];
    }

}
