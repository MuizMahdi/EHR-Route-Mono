export interface BlockHeaderResponse
{
   hash: string;
   previousHash: string;
   timeStamp: number;
   index: number;
   merkleRoot: string;
   networkUUID: string;
}
