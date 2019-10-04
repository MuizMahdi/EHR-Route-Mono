package com.project.EhrRoute.Payload.Auth;
import java.util.List;


public class UserInfo
{
    private Long id;
    private String address;
    private String email;
    private List<String> roles;
    private boolean isFirstLogin;
    private boolean hasAddedInfo;


    public UserInfo(Long id, String address, String email, List<String> roles, boolean isFirstLogin, boolean hasAddedInfo) {
        this.id = id;
        this.address = address;
        this.email = email;
        this.roles = roles;
        this.isFirstLogin = isFirstLogin;
        this.hasAddedInfo = hasAddedInfo;
    }


    public Long getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getAddress() {
        return address;
    }
    public List<String> getRoles() {
        return roles;
    }
    public boolean isFirstLogin() {
        return isFirstLogin;
    }
    public boolean isHasAddedInfo() {
        return hasAddedInfo;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setFirstLogin(boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }
    public void setHasAddedInfo(boolean hasAddedInfo) {
        this.hasAddedInfo = hasAddedInfo;
    }
}
