import { Connection } from 'typeorm';
import { EhrPatientInfo } from './../DataAccess/entities/EHR/EhrPatientInfo';
import { AuthService } from 'src/app/Services/auth.service';
import { DatabaseService } from './../DataAccess/database.service';
import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
})


export class PatientInfoService 
{
   constructor(private dbService:DatabaseService)
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


   public async getUserPateintInfo(userID:number): Promise<EhrPatientInfo>
   {
      // Make sure that a connection is available
      await this.ensurePateintInfoDbConnection(userID);
      
      // Get user's PatientInfo DB connection
      const dbConnection:Connection = await this.dbService.getPatientInfoDbConnection(userID);

      // Get the patient info
      const patientInfo:EhrPatientInfo = await dbConnection.manager.findOne(EhrPatientInfo, 1);

      return patientInfo;
   }

}
