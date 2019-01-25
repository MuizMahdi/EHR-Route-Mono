import { UsersService } from './../../../Services/users.service';
import { Component } from '@angular/core';
import { NodeNetworkService } from 'src/app/Services/node-network.service';
import { NzModalService } from 'ng-zorro-antd';


@Component({
  selector: 'app-nav-search',
  templateUrl: './nav-search.component.html',
  styleUrls: ['./nav-search.component.css']
})


export class NavSearchComponent
{
   // "Network" option is initially selected
   selectedSearchOption:string = "Network"; 

   isSearchOptionsEmpty:boolean;
   searchInputValue:string = "";
   searchOptions = [];

   // Search modal contents visibility booleans
   isUserSearched = false;
   isNetworkSearched = false;
   isProviderSearched = false;
   isEhrSearched = false;


   constructor(
      private userService:UsersService, private networkService:NodeNetworkService,
      private modalService:NzModalService
   ) { }


   onSearchBarInput(value:string): void {
      
      // Search for the selected search bar option with the input value
      switch(this.selectedSearchOption)
      {
         // In case 'User' is selected
         case "User": {
            // Search for users names
            this.searchUsernames(value);
            break;
         }

         case "Network": {
            // Search for networks
            this.searchNetworks(value);
            break;
         }

         case "Provider": {
            this.searchProvidersUsernames(value);
            break;
         }

         default: {
            break;
         }
      }
      
   }


   private searchUsernames(username:string): void
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


   private searchProvidersUsernames(providerUsername:string): void 
   {
      this.userService.searchProviderUsername(providerUsername).subscribe(

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


   onSearch(): void
   {
      // Set all options booleans to false
      this.isUserSearched = false;
      this.isNetworkSearched = false;
      this.isProviderSearched = false;
      this.isEhrSearched = false;


      // Search for the selected search bar option with the input value
      switch(this.selectedSearchOption)
      {
         case "User": {
            // open user profile on modal
            this.getUser();
            break;
         }

         case "Network": {
            // open network profile on modal
            this.getNetwork();
            break;
         }

         case "Provider": {
            // open provider profile on modal
            this.getProvider();
            break;
         }

         default: {
            break;
         }
      }
   }


   getUser()
   {
      // Show user info contents on modal
      this.isUserSearched = true;
   }


   getNetwork()
   {
      // Show network info contents on modal
      this.isNetworkSearched = true;
   }


   getProvider()
   {
      // Show provider info contents on modal
      this.isProviderSearched = true;
   }
}
