import { BlockResponse } from './../Responses/BlockResponse';


export interface UserConsentResponse 
{
   block: BlockResponse;
   userPrivateKey: string;
   userAddress: string;
   providerUUID: string;
   networkUUID: string;
   userID: number;
}