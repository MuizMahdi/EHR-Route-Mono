import { MedicalRecordResponse } from './MedicalRecordResponse';
import { ConsentRequest } from './ConsentRequest';


export interface UpdateConsentRequest
{
   ehrDetailsId: number;
   userConsentRequest: ConsentRequest;
   updateMedicalRecord: MedicalRecordResponse;
}
