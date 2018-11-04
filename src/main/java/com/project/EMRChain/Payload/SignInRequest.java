package com.project.EMRChain.Payload;
import javax.validation.constraints.NotBlank;

public class SignInRequest
{
    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;

    public String getPassword() {
        return password;
    }
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }
}
