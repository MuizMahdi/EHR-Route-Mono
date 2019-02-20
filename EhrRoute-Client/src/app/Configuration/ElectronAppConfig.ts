import { environment } from 'src/environments/environment';
import { remote } from 'electron';
import * as path from 'path';


export class ElectronAppConfig
{
   public static databaseFolderPath: string;
   public static applicationPath: string;
   private static dataFolderPath: string;


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
   }

   
   public static getNetworkChainDbPath(networkUUID:string): string
   {
      let networkDbFileName:string = networkUUID + '.chain';

      return path.join(this.databaseFolderPath, networkDbFileName);
   }

   
   public static getAddressDbPath(userID:number): string
   {
      let addressDbFileName:string = 'user-address-' + userID + '.address';

      return path.join(this.databaseFolderPath, addressDbFileName);
   }


   public static getPatientInfoDbPath(userID:number): string
   {
      let patientInfoDbFileName:string = 'user-info-' + userID + '.info';

      return path.join(this.databaseFolderPath, patientInfoDbFileName);
   }
}