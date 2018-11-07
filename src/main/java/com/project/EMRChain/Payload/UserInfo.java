package com.project.EMRChain.Payload;

public class UserInfo
{
    private Long id;
    private String username;
    private String name;
    private boolean isFirstLogin;

    public UserInfo(Long id, String username, String name, boolean isFirstLogin) {
        this.id = id;
        this.username = username;
        this.name = name;
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

    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
