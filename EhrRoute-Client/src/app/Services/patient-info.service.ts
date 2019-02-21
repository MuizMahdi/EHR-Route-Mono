import { UserInfo } from './../Models/Payload/Responses/UserInfo';
import { AuthService } from 'src/app/Services/auth.service';
import { DatabaseService } from './../DataAccess/database.service';
import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
})


export class PatientInfoService 
{
   constructor(private dbService:DatabaseService, private authService:AuthService)
   { }


   public async ensurePateintInfoDbConnection(userID:number)
   {
      
      // Get user info DB connection
      try
      {
         this.dbService.getPatientInfoDbConnection(userID);
      }
      catch (error)
      {
         // If no connection for user's PatientInfo DB is available
         if ( (<Error>error).name == 'ConnectionNotFoundError' ) {
            // Create a connection
            await this.dbService.createPatientInfoDbConnection(userID);
         }
         else {
            console.log(error);
         }
      }
   }

}
