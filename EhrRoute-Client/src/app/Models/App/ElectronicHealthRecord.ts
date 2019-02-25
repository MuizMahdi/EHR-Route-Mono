import { PatientInfo } from './../Payload/Requests/PatientInfo';


export interface ElectronicHealthRecord
{
   id:number;
   patientData:PatientInfo;
   conditions:any;
   allergies:any;
   history:any;
}
