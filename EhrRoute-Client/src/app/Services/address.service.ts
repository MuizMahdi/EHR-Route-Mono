import { DatabaseService } from './../DataAccess/database.service';
import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
})


export class AddressService 
{
   constructor(private dbService:DatabaseService) 
   { }

   
   public async getUserAddress(): Promise<string>
   {
      return null;
   }
}
