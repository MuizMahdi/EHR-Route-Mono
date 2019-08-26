package com.project.EhrRoute.Payload.Core;
import com.project.EhrRoute.Entities.EHR.MedicalRecord;


public class UserUpdateConsentRequest
{
    private String ehrDetailsUuid;
    private UserConsentRequest userConsentRequest;
    private MedicalRecord updateMedicalRecord;


    public UserUpdateConsentRequest(String ehrDetailsUuid, UserConsentRequest userConsentRequest, MedicalRecord updateMedicalRecord) {
        this.ehrDetailsUuid = ehrDetailsUuid;
        this.userConsentRequest = userConsentRequest;
        this.updateMedicalRecord = updateMedicalRecord;
    }


    public void setUpdateMedicalRecord(MedicalRecord updateMedicalRecord) {
        this.updateMedicalRecord = updateMedicalRecord;
    }
    public void setUserConsentRequest(UserConsentRequest userConsentRequest) {
        this.userConsentRequest = userConsentRequest;
    }
    public void setEhrDetailsUuid(String ehrDetailsUuid) {
        this.ehrDetailsUuid = ehrDetailsUuid;
    }

    public String getEhrDetailsUuid() {
        return ehrDetailsUuid;
    }
    public MedicalRecord getUpdateMedicalRecord() {
        return updateMedicalRecord;
    }
    public UserConsentRequest getUserConsentRequest() {
        return userConsentRequest;
    }
}
