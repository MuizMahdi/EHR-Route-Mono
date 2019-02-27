import { AddressService } from './address.service';
import { ProviderService } from './provider.service';
import { BlockAdditionRequest } from './../Models/Payload/Requests/BlockAdditionRequest';
import ModelMapper from 'src/app/Helpers/Utils/ModelMapper';
import { NodeNetworkService } from 'src/app/Services/node-network.service';
import { BlockResponse } from './../Models/Payload/Responses/BlockResponse';
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
   constructor(
      private dbService:DatabaseService, private networkService:NodeNetworkService,
      private providerService:ProviderService, private addressService:AddressService
   ) { }


   public async generateNetworkMerkleRoot(networkUUID:string): Promise<string>
   {
      // Get the network's leaves hashes for the construction of the merkle tree
      let leavesHexHashes:string[] = await this.getNetworkLeavesHashes(networkUUID); 
      
      // If there's only one leaf in the tree, return that leaf hash
      if (leavesHexHashes.length < 2) 
      {
         return leavesHexHashes[0];
      }

      // Get buffer array from each Hex hash string
      let leavesBuffer:Buffer[] = leavesHexHashes.map(leaf => Buffer.from(leaf, 'hex'));

      // Construct a merkle tree using the buffer array
      let tree = new MerkleTree.default(leavesBuffer, sha256); 

      // Return the root of the tree
      return tree.getRoot().toString('hex');
   }


   private async getNetworkLeavesHashes(networkUUID:string): Promise<string[]>
   {
      // Make sure that a connection for the network DB exists
      await this.ensureNetworkDbConnection(networkUUID);
      
      // Get network's DB conneciton
      let db:Connection = this.dbService.getNetworkDbConnection(networkUUID);

      // Get number of blocks in chain
      const numberOfBlocks:number = await db.manager.count(Block);

      // Blocks hashes (merkle tree leaves) array
      let leavesHexHashes:string[] = [];

      // Get network's DB connection
      const dbConnection:Connection = await this.dbService.getNetworkDbConnection(networkUUID);

      // For each block
      for (let i=1; i<=numberOfBlocks; i++) 
      {
         // Get the leaf hash of the block with index of i
         const blockLeafHash:Block[] = await dbConnection.getRepository(Block).find({
            select: ["merkleLeafHash"],
            where: [{index : i}]
         });

         // Push the leaf hash into the leaves array
         leavesHexHashes.push(blockLeafHash[0].merkleLeafHash);   
      }

      return leavesHexHashes;
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


   public async getNetworkLatestBlock(networkUUID:string): Promise<Block>
   {
      // Make sure that a connection for the network DB exists
      await this.ensureNetworkDbConnection(networkUUID);

      // Get network's DB conneciton
      let dbConnection:Connection = this.dbService.getNetworkDbConnection(networkUUID);

      let blocksCount:number = await dbConnection.manager.count(Block);

      const latestBlock:Block[] = await dbConnection.getRepository(Block).find({
         where: [{index : blocksCount}]
      });

      return latestBlock[0];
   }


   public async addBlock(networkUUID:string, blockResponse:BlockResponse)
   {
      // Make sure that a connection has been established
      this.networkService.ensureNetworkDbConnection(networkUUID);

      // Get a Block from the response
      let block = ModelMapper.mapBlockResponseToBlock(blockResponse);

      // Get DB connection for the network, then save the block
      await this.dbService.getNetworkDbConnection(networkUUID).manager.save(block);
   }


   public async countAllNetworksBlocks(networksUUIDs:string[]): Promise<number>
   {
      let count: number = 0;  

      for (let networkUUID of networksUUIDs) {
         // Get the connection of the network
         let networkDb = this.dbService.getNetworkDbConnection(networkUUID);

         // Genesis block is subtracted
         const networkBlocksCount = await networkDb.getRepository(Block).count()-1;

         // Add the counted blocks of the network
         count = count + networkBlocksCount;
      }

      return count;
   }


   public async generateBlockAdditionRequest(providerUserId:number, ehrUserId:number, networkUuid:string): Promise<BlockAdditionRequest>
   {
      let providerID = providerUserId;
      let ehrUserID = ehrUserId;
      let networkUUID = networkUuid;
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
      await this.generateNetworkMerkleRoot(networkUUID).then(root => {
         merkleRootWithoutBlock = root;
      });

      // Get and set sender address
      await this.addressService.getUserAddress(providerID).then(address => {
         senderAddress = address.address;
      });

      // Get and set block index and previous hash
      await this.getNetworkLatestBlock(networkUUID).then(block => {
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
}
