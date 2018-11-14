package com.project.EMRChain.Payload.Auth;
import javax.validation.constraints.NotBlank;

public class RoleChangeRequest
{
    @NotBlank
    private String username;

    @NotBlank
    private String role;

    public String getRole() {
        return role;
    }
    public String getUsername() {
        return username;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
