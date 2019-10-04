package com.project.EhrRoute.Payload.Auth;
import javax.validation.constraints.NotBlank;

public class RoleChangeRequest
{
    @NotBlank
    private String address;

    @NotBlank
    private String role;

    public String getRole() {
        return role;
    }
    public String getAddress() {
        return address;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
