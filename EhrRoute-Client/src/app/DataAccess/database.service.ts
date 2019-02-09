import { Injectable } from "@angular/core";
import { Connection, ConnectionOptions, createConnection, getConnectionManager } from 'typeorm';
import { ElectronAppConfig } from "../Configuration/ElectronAppConfig";
import { Block } from "./entities/Core/Block";
import { MedicalRecord } from "./entities/EHR/MedicalRecord";
import { EhrAllergyAndReaction } from "./entities/EHR/EhrAllergyAndReaction";
import { EhrCondition } from "./entities/EHR/EhrCondition";
import { EhrHistory } from "./entities/EHR/EhrHistory";
import { EhrPatientInfo } from "./entities/EHR/EhrPatientInfo";
import { Address } from "./entities/Core/Address";


@Injectable({
   providedIn: 'root'
})


/* 
*   A Network DB file is created for each database.
*   A connection is created for each network's DB on startup
*/


export class DatabaseService 
{
   
   constructor() 
   { }


   // Creates a connection to the network db with networkUUID
   public async createNetworkDbConnection(networkUUID:string)
   {
      let dbOptions:ConnectionOptions = {
         name: this.getNetConnectionName(networkUUID),
         type: "sqlite",
         database: ElectronAppConfig.getNetworkChainDbPath(networkUUID),
         entities: [
            Block,
            MedicalRecord,
            EhrAllergyAndReaction,
            EhrCondition,
            EhrHistory,
            EhrPatientInfo
         ],
         synchronize: true,
         logging: false
      }

      // If a connection has already been established
      await createConnection(dbOptions);
   }


   // Returns a connection for the network with networkUUID
   public getNetworkDbConnection(networkUUID:string): Connection
   {
      return getConnectionManager().get(this.getNetConnectionName(networkUUID));
   }


   // Attaches a "-connection" prefix to networkUUID to form connection name for a network
   private getNetConnectionName(networkUUID:string): string
   {
      return networkUUID + "-connection";
   }


   // Creates a connection to the address db with user ID
   public async createAddressDbConnection(userID:number)
   {
      let dbOptions:ConnectionOptions = {
         name: this.getAddressConnectionName(userID),
         type: "sqlite",
         database: ElectronAppConfig.getAddressDbPath(userID),
         entities: [
            Address,
         ],
         synchronize: true,
         logging: false
      }

      await createConnection(dbOptions);
   }


   // Returns a connection for address DB of user with an ID
   public getAddressDbConnection(userID:number): Connection
   {
      return getConnectionManager().get(this.getAddressConnectionName(userID));
   }


   // Attaches a "-address" prefix to a user ID to form connection name for a user address DB
   private getAddressConnectionName(userID:number): string
   {
      return userID + "-address";
   }

}
