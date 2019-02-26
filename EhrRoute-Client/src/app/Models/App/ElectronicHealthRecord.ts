import { HealthRecordData } from './HealthRecordData';
import { BlockInfo } from './BlockInfo';


export interface ElectronicHealthRecord
{
   blockInfo: BlockInfo;
   recordData: HealthRecordData;
}