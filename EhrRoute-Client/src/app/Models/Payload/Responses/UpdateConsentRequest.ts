import { MedicalRecordResponse } from './MedicalRecordResponse';
import { ConsentRequest } from './ConsentRequest';


export interface UpdateConsentRequest
{
   userConsentRequest: ConsentRequest;
   updateMedicalRecord: MedicalRecordResponse;
}
