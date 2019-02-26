import { PatientInfo } from '../Payload/Requests/PatientInfo';


export interface HealthRecordData
{
   id:number;
   patientData:PatientInfo;
   conditions:any;
   allergies:any;
   history:any;
}
