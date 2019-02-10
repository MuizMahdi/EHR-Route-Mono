import { BlockAdditionRequest } from './../../../Models/Payload/Requests/BlockAdditionRequest';
import { AuthService } from 'src/app/Services/auth.service';
import { AddressService } from './../../../Services/address.service';
import { ChainService } from './../../../Services/chain.service';
import { ProviderService } from './../../../Services/provider.service';
import { UserNetworks } from './../../../Models/Payload/Responses/UserNetworks';
import { NodeNetworkService } from './../../../Services/node-network.service';
import { NzModalService, NzModalRef } from 'ng-zorro-antd';
import { ErrorResponse } from 'src/app/Models/Payload/Responses/ErrorResponse';
import { UserInfo } from './../../../Models/Payload/Responses/UserInfo';
import { UsersService } from './../../../Services/users.service';
import { Component, OnInit, Input, TemplateRef } from '@angular/core';
import { NetworkInfo } from 'src/app/Models/Payload/Responses/NetworkInfo';


@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})


export class UserProfileComponent implements OnInit
{
   // Searched username
   @Input() searchParameter: string;

   searchedUser:UserInfo = null;

   selectedNetwork:any = {};
   userNetworks:NetworkInfo[];
   userHasNetwork:boolean = false;
   networkSelectionModal: NzModalRef;


   constructor(
      private userService:UsersService, private modalService:NzModalService, 
      private networkService:NodeNetworkService, private providerService:ProviderService,
      private chainService:ChainService, private addressService:AddressService,
      private authService:AuthService
   ) { }


   ngOnInit()
   {
      this.getSearchedUserProfile();
      this.getCurrentUserNetworks();
   }


   private getSearchedUserProfile(): void
   {
      if (this.searchParameter) {

         // Get user profile
         this.userService.getUserInfo(this.searchParameter).subscribe(

            (response:UserInfo) => {
               this.searchedUser = response;
            },

            (error:ErrorResponse) => {
               console.log(error);
            }

         );
      }
   }


   private getCurrentUserNetworks(): void 
   {
      this.networkService.getUserNetworks().subscribe(

         (response:UserNetworks) => {
            // If network response is received then user has a network.
            this.userHasNetwork = true;

            // Get user networks from response
            this.userNetworks = response.userNetworks;

            // Select the first network as default selected network for the network selector
            this.selectedNetwork = this.userNetworks[0];
         },

         (error:ErrorResponse) => {
            // Http NOT_FOUND status is returned when user has no networks
            if (error.httpStatus === 404) {
               this.userHasNetwork = false;
            }
         }

      );
   }


   private requestEhrPrivilege(): void
   {
      this.getBlockAdditionRequest().then(blockAdditionRequest => {
         
         this.userService.sendUserEhrConsentRequest(blockAdditionRequest).subscribe(

            response => {
               console.log(response);
            },

            (error:ErrorResponse) => {
               console.log(error);
            }

         );
         
      })
   }


   private async getBlockAdditionRequest(): Promise<BlockAdditionRequest>
   {
      let userID = this.authService.getCurrentUser().id;
      let ehrUserID = this.searchedUser.id;
      let networkUUID = this.selectedNetwork.networkUUID;
      let merkleRootWithoutBlock:string = "";
      let previousBlockHash:string = "";
      let previousBlockIndex:number;
      let senderAddress:string = "";
      let providerUUID:string = "";

      // Get and set provider UUID
      await this.getCurrentProviderUUID().then(uuid => {
         providerUUID = uuid;
      });

      // Get and set merkle root
      await this.chainService.generateNetworkMerkleRoot(networkUUID).then(root => {
         merkleRootWithoutBlock = root;
      });

      // Get and set sender address
      await this.addressService.getUserAddress(userID).then(address => {
         senderAddress = address.address;
      });

      // Get and set block index and previous hash
      await this.chainService.getNetworkLatestBlock(networkUUID).then(block => {
         previousBlockIndex = block.index;
         previousBlockHash = block.hash;
      });

      // Construct a block addition object
      let blockAdditionRequest:BlockAdditionRequest = {
         chainRootWithoutBlock:merkleRootWithoutBlock,
         previousBlockIndex: previousBlockIndex.toString(),
         previousBlockHash: previousBlockHash,
         senderAddress: senderAddress,
         providerUUID: providerUUID,
         networkUUID: networkUUID,
         ehrUserID: ehrUserID.toString()
      }

      return blockAdditionRequest;
   }


   private async getCurrentProviderUUID(): Promise<string>
   {
      let providerUUID:string;
      
      await this.providerService.getCurrentProviderUUID().then(
         
         response => {
            providerUUID = response.payload;
         })

         .catch(error => {
            console.log(error);
         }

      );
      
      return providerUUID;
   }


   private createNetworkSelectionModal(templateContent: TemplateRef<{}>): void 
   {
      this.networkSelectionModal = this.modalService.create({
         nzTitle: "Network Selection",
         nzContent: templateContent,
         nzOnOk: () => this.requestEhrPrivilege()
      });
   }


   private destroyNetworkSelectionModal(): void 
   {
      this.networkSelectionModal.destroy();
   }
}
