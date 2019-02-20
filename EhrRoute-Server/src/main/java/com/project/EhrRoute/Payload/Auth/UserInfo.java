package com.project.EhrRoute.Payload.Auth;
import java.util.List;


public class UserInfo
{
    private Long id;
    private String username;
    private String name;
    private String email;
    private List<String> roles;
    private boolean isFirstLogin;
    private boolean hasAddedInfo;


    public UserInfo(Long id, String username, String name, String email, List<String> roles, boolean isFirstLogin, boolean hasAddedInfo) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.isFirstLogin = isFirstLogin;
        this.hasAddedInfo = hasAddedInfo;
    }


    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getUsername() {
        return username;
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
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setFirstLogin(boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }
    public void setHasAddedInfo(boolean hasAddedInfo) {
        this.hasAddedInfo = hasAddedInfo;
    }
}
