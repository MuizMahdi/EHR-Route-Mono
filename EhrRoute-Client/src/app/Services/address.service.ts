import { ErrorResponse } from './../Models/Payload/Responses/ErrorResponse';
import { UserInfo } from './../Models/Payload/Responses/UserInfo';
import { AuthService } from './auth.service';
import { Address } from './../DataAccess/entities/Core/Address';
import { Connection } from 'typeorm';
import { AddressResponse } from './../Models/Payload/Responses/AddressResponse';
import { HttpClient } from '@angular/common/http';
import { DatabaseService } from './../DataAccess/database.service';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { first, catchError } from 'rxjs/operators';
import ModelMapper from '../Helpers/Utils/ModelMapper';


@Injectable({
  providedIn: 'root'
})


export class AddressService 
{
   private addressGenerationUrl:string = environment.apiUrl + '/address/generate';


   constructor(
      private dbService:DatabaseService, private http:HttpClient, 
      private authService:AuthService) 
   { }


   public async ensureAddressDBsConnection()
   {
      this.authService.getCurrentUserInfo().subscribe(

         (userInfo:UserInfo) => {
            // Get current user ID
            let userID = userInfo.id;
            this.checkAddressDbConnection(userID);
         },

         (error:ErrorResponse) => {
            console.log(error);
         }

      );
   }


   private async checkAddressDbConnection(userID:number)
   {
      // Get the DB connection of address's DB
      try
      {
         this.dbService.getAddressDbConnection(userID);
      }
      catch (error)
      {
         // If no connection for network's DB is available, then create a connection
         if ( (<Error>error).name == 'ConnectionNotFoundError' ) {
            await this.dbService.createAddressDbConnection(userID);
         }
         else {
            console.log(error);
         }
      }
   }


   public generateUserAddress(): Observable<any>
   {
      return this.http.get(this.addressGenerationUrl).pipe(first(),

         catchError(error => {
            return throwError(error);
         })

      );
   }

   
   public async getUserAddress(userID:number): Promise<Address>
   {
      try
      {
         // Create an address DB/Connection
         await this.dbService.createAddressDbConnection(userID);
      }
      catch(error)
      {
         // Create an address DB/Connection
         await this.checkAddressDbConnection(userID);
      }
      
      // Get the address DB of user connection
      const dbConnection:Connection = await this.dbService.getAddressDbConnection(userID);

      const address:Address = await dbConnection.manager.findOne(Address, 1);

      return address;
   }


   public async saveUserAddress(addressResponse:AddressResponse, userID:number)
   {
      // Create an address DB/Connection
      await this.dbService.createAddressDbConnection(userID);

      // Get the Connection
      const dbConnection:Connection = await this.dbService.getAddressDbConnection(userID);

      // Map the address response to address
      let address:Address = ModelMapper.mapAddressResponseToAddress(addressResponse);

      // Persist the address response data
      await dbConnection.manager.save(address);
   }
}
