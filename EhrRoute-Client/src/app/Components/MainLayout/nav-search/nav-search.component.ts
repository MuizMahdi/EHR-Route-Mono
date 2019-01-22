import { UsersService } from './../../../Services/users.service';
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
   isSearchOptionsEmpty:boolean;
   searchInputValue:string = "";
   searchOptions = [];


   constructor(private userService:UsersService) 
   { }


   ngOnInit() 
   { }

   
   onSearch()
   {
      console.log(this.selectedSearchOption);
      console.log(this.searchInputValue);
   }


   onInput(value: string): void {
      
      // If user option is selected on search bar
      if (this.selectedSearchOption === "User") 
      {
         // Search for username with input value
         this.userService.searchUsername(value).subscribe(

            (response:string[]) => {
   
               if (response.length > 0) {
                  this.isSearchOptionsEmpty = false;
                  this.searchOptions = response;
               } 
               else {
                  this.searchOptions = [""];
                  this.isSearchOptionsEmpty = true;
               }
               
            },
   
            error => {
               console.log(error);
            }
   
         );
      }
      

   }

   onSelectedSearchOptionChange() {
      // Reset the found values from previous option
      this.isSearchOptionsEmpty = true;
      this.searchOptions = null;
   }

}
