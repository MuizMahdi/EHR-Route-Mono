package com.project.EhrRoute.Payload.Auth;
import java.util.List;


public class UserInfo
{
    private Long id;
    private String username;
    private String name;
    private List<String> roles;
    private boolean isFirstLogin;


    public UserInfo(Long id, String username, String name, List<String> roles, boolean isFirstLogin) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.roles = roles;
        this.isFirstLogin = isFirstLogin;
    }


    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
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

    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
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
}
