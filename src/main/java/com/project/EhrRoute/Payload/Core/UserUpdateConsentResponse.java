package com.project.EhrRoute.Payload.Core;


public class UserUpdateConsentResponse
{
    private String ehrDetailsUuid;
    private UserConsentResponse consentResponse;


    public UserUpdateConsentResponse() { }
    public UserUpdateConsentResponse(String ehrDetailsUuid, UserConsentResponse consentResponse) {
        this.ehrDetailsUuid = ehrDetailsUuid;
        this.consentResponse = consentResponse;
    }


    public String getEhrDetailsUuid() {
        return ehrDetailsUuid;
    }
    public UserConsentResponse getConsentResponse() {
        return consentResponse;
    }
    public void setEhrDetailsUuid(String ehrDetailsUuid) {
        this.ehrDetailsUuid = ehrDetailsUuid;
    }
    public void setConsentResponse(UserConsentResponse consentResponse) {
        this.consentResponse = consentResponse;
    }
}
