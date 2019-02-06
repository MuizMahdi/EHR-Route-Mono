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
      await this.ensureNetworkDbConnection(networkUUID);
      
      // Get network's DB conneciton
      let db:Connection = this.dbService.getNetworkDbConnection(networkUUID);

      // Get number of blocks in chain
      const numberOfBlocks:number = await db.manager.count(Block);

      // Blocks hashes (merkle tree leaves) array
      let leaves:string[];

      // TODO: THIS :- 

      // For each block
      for (let i = 0; i<=numberOfBlocks; i++) 
      {
         // Get DB connection for the network, then get the block with ID = i
         //await this.dbService.getNetworkDbConnection(networkUUID).manager.find({select: ""})

         // Push the block's hash into the leaves array
         
      }

      // Generate merkle root from leaves
      
   }


   private async ensureNetworkDbConnection(networkUUID:string)
   {
      
      try 
      {
         // Get network DB connection
         await this.dbService.getNetworkDbConnection(networkUUID);
      }
      catch (error) 
      { 

         // If connection doesn't exists
         if ( (<Error>error).name == 'ConnectionNotFoundError' ) {

            // Create connection
            await this.dbService.createNetworkDbConnection(networkUUID);

         }
         // Any other error
         else {
            console.log(error);
         }

      }

   }
}
