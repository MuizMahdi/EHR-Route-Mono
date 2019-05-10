package com.project.EhrRoute.Payload.Core;
import com.project.EhrRoute.Entities.EHR.MedicalRecord;


public class UserUpdateConsentRequest
{
    private Long ehrDetailsId;
    private UserConsentRequest userConsentRequest;
    private MedicalRecord updateMedicalRecord;


    public UserUpdateConsentRequest(Long ehrDetailsId, UserConsentRequest userConsentRequest, MedicalRecord updateMedicalRecord) {
        this.ehrDetailsId = ehrDetailsId;
        this.userConsentRequest = userConsentRequest;
        this.updateMedicalRecord = updateMedicalRecord;
    }


    public void setUpdateMedicalRecord(MedicalRecord updateMedicalRecord) {
        this.updateMedicalRecord = updateMedicalRecord;
    }
    public void setUserConsentRequest(UserConsentRequest userConsentRequest) {
        this.userConsentRequest = userConsentRequest;
    }
    public void setEhrDetailsId(Long ehrDetailsId) {
        this.ehrDetailsId = ehrDetailsId;
    }

    public Long getEhrDetailsId() {
        return ehrDetailsId;
    }
    public MedicalRecord getUpdateMedicalRecord() {
        return updateMedicalRecord;
    }
    public UserConsentRequest getUserConsentRequest() {
        return userConsentRequest;
    }
}
