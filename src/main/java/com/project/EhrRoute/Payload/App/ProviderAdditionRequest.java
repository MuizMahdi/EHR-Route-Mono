package com.project.EhrRoute.Payload.App;

public class ProviderAdditionRequest
{
    private String address;
    private String institutionName;


    public ProviderAdditionRequest() { }
    public ProviderAdditionRequest(String address, String institutionName) {
        this.address = address;
        this.institutionName = institutionName;
    }


    public String getAddress() {
        return address;
    }
    public String getInstitutionName() {
        return institutionName;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }
}
