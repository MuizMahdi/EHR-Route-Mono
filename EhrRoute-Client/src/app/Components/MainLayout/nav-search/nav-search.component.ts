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


   onSearchBarInput(value:string): void {
      
      // If user option is selected on search bar
      if (this.selectedSearchOption === "User") 
      {
         // Search for username with input value
         this.searchUsersnames(value);
      }  
      
      // TODO: Use a Select instead of multiple Ifs to check selectedSearchOption values 
   }


   private searchUsersnames(username:string)
   {
      this.userService.searchUsername(username).subscribe(

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


   private onSelectedSearchOptionChange() {
      // Reset the found values from previous option
      this.isSearchOptionsEmpty = true;
      this.searchOptions = null;
   }

}
