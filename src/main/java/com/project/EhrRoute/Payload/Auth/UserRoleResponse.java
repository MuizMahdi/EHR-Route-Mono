package com.project.EhrRoute.Payload.Auth;

public class UserRoleResponse
{
    private String roleName;

    public UserRoleResponse(String roleName)
    {
        this.setRoleName(roleName);
    }

    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}