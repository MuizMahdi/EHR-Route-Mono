import { Block } from './../DataAccess/entities/Core/Block';
import { Connection } from 'typeorm';
import { DatabaseService } from 'src/app/DataAccess/database.service';
import { Injectable } from '@angular/core';
import * as MerkleTree from 'merkletreejs';
import sha256 from 'crypto-js/sha256';


@Injectable({
  providedIn: 'root'
})


export class ChainService 
{
   constructor(private dbService:DatabaseService) 
   { }


   public async generateNetworkMerkleRoot(networkUUID:string)
   {
      // Make sure that a connection for the network DB exists
      await this.isNetworkDbConnectioAvailable(networkUUID)
      
      // Get network's DB conneciton
      let db:Connection = this.dbService.getNetworkDbConnection(networkUUID);

      const numberOfBlocks = await db.manager.count(Block);
    
      console.log("NUMBER OF BLOCKS: " + numberOfBlocks);
      
   }


   private async isNetworkDbConnectioAvailable(networkUUID:string)
   {
      
      try {

         // Create connection
         await this.dbService.createNetworkDbConnection(networkUUID);

      }
      catch (error) { 

         // If connection already exists
         if ( (<Error>error).name == 'AlreadyHasActiveConnectionError' ) {
            return;
         }
         // Any other error
         else {
            console.log(error);
            return;
         }

      }

   }
}
