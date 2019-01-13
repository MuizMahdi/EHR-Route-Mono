import { Injectable } from "@angular/core";
import { Connection, ConnectionOptions, createConnection } from 'typeorm';
import { ElectronAppConfig } from "../Configuration/ElectronAppConfig";


@Injectable({
   providedIn: 'root'
})


export class DatabaseService 
{
   public chainDbconnection: Promise<Connection>;
   public addressDbConnection: Promise<Connection>;
   private readonly chainDbOptions: ConnectionOptions;
   private readonly addressDbOpeitons: ConnectionOptions;

   constructor() {

      ElectronAppConfig.initialize();

      this.chainDbOptions = {
         type: "sqlite",
         database: ElectronAppConfig.chainDbPath,
         entities: [],
         synchronize: true,
         logging: false,
      };

      this.addressDbOpeitons = {
         type: "sqlite",
         database: ElectronAppConfig.addressDbPath,
         entities: [],
         synchronize: true,
         logging: false,
      };

      this.chainDbconnection = createConnection(this.chainDbOptions);
      this.addressDbConnection = createConnection(this.addressDbOpeitons);
   }
}
