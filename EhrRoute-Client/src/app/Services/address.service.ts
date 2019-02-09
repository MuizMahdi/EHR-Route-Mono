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


   constructor(private dbService:DatabaseService, private http:HttpClient) 
   { }


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
      // Get the address DB of user connection
      const dbConnection:Connection = await this.dbService.getAddressDbConnection(userID);

      // Get the address object from DB
      const address:Address[] = await dbConnection.getRepository(Address).find({
         where: [{index : 1}]
      });

      return address[0];
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
