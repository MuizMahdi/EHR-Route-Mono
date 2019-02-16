package com.project.EhrRoute.Payload.App;

public class ProviderAdditionRequest
{
    private String username;
    private String institutionName;


    public ProviderAdditionRequest() { }
    public ProviderAdditionRequest(String username, String institutionName) {
        this.username = username;
        this.institutionName = institutionName;
    }


    public String getUsername() {
        return username;
    }
    public String getInstitutionName() {
        return institutionName;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }
}
