package com.project.EhrRoute.Payload.Auth;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignUpRequest
{
    private String providerToken;

    @NotBlank
    @Size(max=40)
    @Email
    private String email;

    @NotBlank
    @Size(min=6, max=20)
    private String password;

    public String getEmail() {
        return email;
    }
    public String getProviderToken() {
        return providerToken;
    }
    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setProviderToken(String providerToken) {
        this.providerToken = providerToken;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
