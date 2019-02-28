import { RecordUpdateData } from './RecordUpdateData';
import { BlockAdditionRequest } from './BlockAdditionRequest';


export interface UpdatedBlockAdditionRequest
{
   blockAddition: BlockAdditionRequest;
   recordUpdateData: RecordUpdateData;
}