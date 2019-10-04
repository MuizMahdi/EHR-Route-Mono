package com.project.EhrRoute.Payload.Auth;
import javax.validation.constraints.NotBlank;

public class SignInRequest
{
    @NotBlank
    private String addressOrEmail;

    @NotBlank
    private String password;

    public String getPassword() {
        return password;
    }
    public String getAddressOrEmail() {
        return addressOrEmail;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setAddressOrEmail(String addressOrEmail) {
        this.addressOrEmail = addressOrEmail;
    }
}
