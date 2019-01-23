import { UsersService } from './../../../Services/users.service';
import { Component, OnInit } from '@angular/core';
import { NodeNetworkService } from 'src/app/Services/node-network.service';


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


   constructor(private userService:UsersService, private networkService:NodeNetworkService) 
   { }


   ngOnInit() 
   { }

   
   onSearch(): void
   {
      console.log(this.selectedSearchOption);
      console.log(this.searchInputValue);
   }


   onSearchBarInput(value:string): void {
      
      // Search for the selected search bar option with the input value
      switch(this.selectedSearchOption)
      {
         // In case 'User' is selected
         case "User": {
            // Search for users names
            this.searchUsersnames(value);
            break;
         }

         case "Network": {
            // Search for networks
            this.searchNetworks(value);
            break;
         }

         default: {
            break;
         }
      }
      
   }


   private searchUsersnames(username:string): void
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


   private searchNetworks(networkName:string): void
   {
      this.networkService.searchNetworksByName(networkName).subscribe(

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


   private onSelectedSearchOptionChange(): void {
      // Reset the found values from previous option
      this.isSearchOptionsEmpty = true;
      this.searchOptions = null;
   }

}
