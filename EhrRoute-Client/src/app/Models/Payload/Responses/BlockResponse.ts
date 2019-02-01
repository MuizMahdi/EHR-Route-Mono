import { BlockHeaderResponse } from "./BlockHeaderResponse";
import { TransactionResponse } from "./TransactionResponse";


export interface BlockResponse
{
   blockHeader: BlockHeaderResponse;
   transaction: TransactionResponse;
}
