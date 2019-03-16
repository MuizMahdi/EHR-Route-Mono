import { ErrorResponse } from 'src/app/Models/Payload/Responses/ErrorResponse';
import { environment } from 'src/environments/environment';
import { Injectable } from '@angular/core';
import { ElectronAppConfig } from '../Configuration/ElectronAppConfig';
import { ElectronService } from 'ngx-electron';


// NodeJs FileSystem
declare var fs: any;


@Injectable({
  providedIn: 'root'
})


export class ChainFileService 
{
   chainSendUrl:string = environment.apiUrl + '/chain'


   constructor(private electronService:ElectronService) 
   { }

   
   public sendNetworkChain(networkUUID:string, consumerUUID:string): any
   {
      let filePath:string = ElectronAppConfig.getNetworkChainDbPath(networkUUID);

      let url = this.chainSendUrl + '?consumeruuid=' + consumerUUID + '&networkuuid=' + networkUUID;

      fs.readFile(filePath, (error, fileData) => {

         if (error) {
            console.log(error);
         }

         this.uploadChainFile(fileData, url);

      });

   }


   uploadChainFile(file:any, uploadUri:string): any {

      let formData = new FormData();

      formData.append('file', new Blob([file]));

      const options = {
         method: 'POST',
         body: formData
      };

      fetch(uploadUri, options).then(

         (success) => console.log(success)

      ).catch(

         (error:ErrorResponse) => console.log(error)
         
      );
      
   }


   downloadChainFile(chainURL:string) {

      if(this.electronService.isElectronApp) {

         // Start download
         this.electronService.ipcRenderer.send('download', {
            url: chainURL,
            properties: {directory: ElectronAppConfig.databaseFolderPath}
         });

         // On download complete
         this.electronService.ipcRenderer.on('DownloadComplete', () => {
            console.log('[ Chain file download has completed ]');
         });

      }

   }
}
