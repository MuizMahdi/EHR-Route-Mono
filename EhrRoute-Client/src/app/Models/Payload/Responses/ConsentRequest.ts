import { BlockResponse } from './BlockResponse';

export interface ConsentRequest
{
   block: BlockResponse;
   networkUUID: string;
   providerUUID: string;
   userID: number;
}