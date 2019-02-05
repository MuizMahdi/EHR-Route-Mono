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


   public async generateNetworkMerkleRoot(networkUUID:string): string
   {
      // Make sure that a connection for the network DB exists
      try {
         await this.dbService.createNetworkDbConnection(networkUUID);
      }
      catch (error) { // If connection already exists
         console.log(error);
      }
      

      // Get network's DB conneciton
      let db:Connection = this.dbService.getNetworkDbConnection(networkUUID);


      const numberOfBlocks = db.manager.query("SELECT COUNT * FROM BLOCK");
    
      console.log("NUMBER OF BLOCKS: " + numberOfBlocks);
   }
}
