package com.project.EhrRoute.Payload.Core;
import com.project.EhrRoute.Entities.EHR.MedicalRecord;


public class UserUpdateConsentRequest
{
    private UserConsentRequest userConsentRequest;
    private MedicalRecord updateMedicalRecord;


    public UserUpdateConsentRequest(UserConsentRequest userConsentRequest, MedicalRecord updateMedicalRecord) {
        this.userConsentRequest = userConsentRequest;
        this.updateMedicalRecord = updateMedicalRecord;
    }


    public void setUserConsentRequest(UserConsentRequest userConsentRequest) {
        this.userConsentRequest = userConsentRequest;
    }
    public void setUpdateMedicalRecord(MedicalRecord updateMedicalRecord) {
        this.updateMedicalRecord = updateMedicalRecord;
    }

    public MedicalRecord getUpdateMedicalRecord() {
        return updateMedicalRecord;
    }
    public UserConsentRequest getUserConsentRequest() {
        return userConsentRequest;
    }
}
