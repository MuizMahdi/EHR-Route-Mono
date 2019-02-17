package com.project.EhrRoute.Payload.Auth;
import java.util.List;


public class UserInfo
{
    private Long id;
    private String username;
    private String name;
    private List<String> roles;
    private boolean isNonFirstLogin;


    public UserInfo(Long id, String username, String name, List<String> roles, boolean isNonFirstLogin) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.roles = roles;
        this.isNonFirstLogin = isNonFirstLogin;
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
    public boolean isNonFirstLogin() {
        return isNonFirstLogin;
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
    public void setNonFirstLogin(boolean isNonFirstLogin) {
        isNonFirstLogin = isNonFirstLogin;
    }
}
