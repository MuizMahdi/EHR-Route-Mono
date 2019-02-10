export interface BlockAdditionRequest
{
   chainRootWithoutBlock: string;
   previousBlockIndex: string;
   previousBlockHash: string;
   senderAddress: string;
   providerUUID: string;
   networkUUID: string;
   ehrUserID: string;
}