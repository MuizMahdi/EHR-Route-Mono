package com.project.EhrRoute.Payload.App;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ProviderInstitutionAdditionRequest
{
    @NotBlank @NotNull private String address;
    @NotBlank @NotNull private String institutionName;

    public ProviderInstitutionAdditionRequest() { }
    public ProviderInstitutionAdditionRequest(String address, String institutionName) {
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
