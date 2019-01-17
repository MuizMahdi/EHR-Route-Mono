import { environment } from 'src/environments/environment';
import { remote } from 'electron';
import * as path from 'path';


export class ElectronAppConfig
{
   public static databaseFolderPath: string;
   public static applicationPath: string;
   private static dataFolderPath: string;
   public static addressDbPath: string
   public static chainDbPath: string;

   private static chainDbName = 'node-records.chain';
   private static addressDbName = 'user-address.keys';


   public static initialize(): void 
   {
      this.getPaths();
   }


   private static getPaths() 
   {
      // Use 'userData' electron path to store db files on production
      if(environment.production) 
      {
         this.dataFolderPath = '/';
         this.applicationPath = remote.app.getPath('userData');
      } 
      else {
         // Use dist/assets/data to store database files for development
         this.dataFolderPath = 'dist/assets/data';
         this.applicationPath = remote.app.getAppPath();
      }

      this.databaseFolderPath = path.join(this.applicationPath, this.dataFolderPath);

      // Path to the chain/records db file
      this.chainDbPath = path.join(this.databaseFolderPath, this.chainDbName);

      // Path to the address/keys db file
      this.addressDbPath = path.join(this.databaseFolderPath, this.addressDbName);
   }

   
   public static getNetworkChainDbPath(networkUUID:string): string
   {
      let networkDbFileName:string = networkUUID + '.chain';

      return path.join(this.databaseFolderPath, networkDbFileName);
   }
}