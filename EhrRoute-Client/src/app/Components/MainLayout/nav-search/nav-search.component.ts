import { Component } from '@angular/core';
import { NzModalService } from 'ng-zorro-antd';
import { UsersService } from './../../../Services/users.service';
import { NodeNetworkService } from 'src/app/Services/node-network.service';
import { UserProfileComponent } from './../../Searches/user-profile/user-profile.component';


@Component({
  selector: 'app-nav-search',
  templateUrl: './nav-search.component.html',
  styleUrls: ['./nav-search.component.css']
})


export class NavSearchComponent
{
   // Network search option is initially selected
   selectedSearchOption:string = "Network"; 

   isSearchOptionsEmpty:boolean;
   searchInputValue:string = "";
   searchOptions = [];


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


   private onSearch(): void
   {
      // Search for the selected search bar option with the input value
      switch(this.selectedSearchOption)
      {
         case "User": {
            // open user profile on modal
            this.showUserInfo();
            break;
         }

         case "Network": {
            // open network profile on modal
            this.showNetworkInfo();
            break;
         }

         case "Provider": {
            // open provider profile on modal
            this.showProviderInfo();
            break;
         }

         default: {
            break;
         }
      }
   }


   private showUserInfo()
   {
      // Show user info contents on modal
      this.showSearchResultsModal(UserProfileComponent, this.searchInputValue);
   }


   private showNetworkInfo()
   {
      // Show network info contents on modal
      this.showSearchResultsModal(null, this.searchInputValue);
   }


   private showProviderInfo()
   {
      // Show provider info contents on modal
      this.showSearchResultsModal(null, this.searchInputValue);
   }


   private showSearchResultsModal(searchResultComponent:any, searchParameter:string) 
   {
      // searchResultParameter: could be a username, network name, provider username, or an EHR ID

      const searchResultModal = this.modalService.create({

         nzTitle: null,

         nzContent: searchResultComponent,

         // Pass the parameter to the component as an input
         nzComponentParams: {
            searchParameter: searchParameter
         },

         nzFooter: null,

         nzWidth: '35vw'

      });

      searchResultModal.afterOpen.subscribe(() => console.log('Notification Modal Opened'));
      searchResultModal.afterClose.subscribe(() => console.log('Notification Modal Closed'));

      // delay until modal instance created
      window.setTimeout(() => {
        const instance = searchResultModal.getContentComponent();
      }, 2000);
   }
}
