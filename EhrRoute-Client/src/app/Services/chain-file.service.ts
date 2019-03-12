import { environment } from 'src/environments/environment';
import { Injectable } from '@angular/core';
import { ElectronAppConfig } from '../Configuration/ElectronAppConfig';


// NodeJs FileSystem
declare var fs: any;


@Injectable({
  providedIn: 'root'
})


export class ChainFileService 
{
   chainSendUrl:string = environment.apiUrl + '/chain/chaingive'


   constructor() { }

   
   public sendNetworkChain(networkUUID:string): any
   {
      let file:any;
      let filePath:string = ElectronAppConfig.getNetworkChainDbPath(networkUUID);

      let url = this.chainSendUrl + '?consumer=' + 'consumerUUID';

      fs.readFile(filePath, (error, data) => {

         if (error) {
            console.log(error);
         }

         console.log("Chain File: ");
         console.log(data);

      });

   }
}
