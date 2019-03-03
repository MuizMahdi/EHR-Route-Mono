package com.project.EhrRoute.Payload.Core;


public class UserUpdateConsentResponse
{
    private Long ehrDetailsId;
    private UserConsentResponse consentResponse;


    public UserUpdateConsentResponse() { }
    public UserUpdateConsentResponse(Long ehrDetailsId, UserConsentResponse consentResponse) {
        this.ehrDetailsId = ehrDetailsId;
        this.consentResponse = consentResponse;
    }


    public Long getEhrDetailsId() {
        return ehrDetailsId;
    }
    public UserConsentResponse getConsentResponse() {
        return consentResponse;
    }
    public void setEhrDetailsId(Long ehrDetailsId) {
        this.ehrDetailsId = ehrDetailsId;
    }
    public void setConsentResponse(UserConsentResponse consentResponse) {
        this.consentResponse = consentResponse;
    }
}
