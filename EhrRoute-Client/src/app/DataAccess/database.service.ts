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
   private readonly addressDbOptions: ConnectionOptions;

   constructor() {

      ElectronAppConfig.initialize();

      this.chainDbOptions = {
         type: "sqlite",
         database: ElectronAppConfig.chainDbPath,
         entities: [__dirname + "/entities/**/*.ts"],
         synchronize: true,
         logging: false
      };

      this.addressDbOptions = {
         type: "sqlite",
         database: ElectronAppConfig.addressDbPath,
         entities: [__dirname + "/entities/**/*.ts"],
         synchronize: true,
         logging: false
      };

      this.chainDbconnection = createConnection(this.chainDbOptions);
      this.addressDbConnection = createConnection(this.addressDbOptions);
   }
}
