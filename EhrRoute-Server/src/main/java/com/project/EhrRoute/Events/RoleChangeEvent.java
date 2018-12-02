package com.project.EMRChain.Events;
import com.project.EMRChain.Entities.Auth.User;
import org.springframework.context.ApplicationEvent;

public class RoleChangeEvent extends ApplicationEvent
{
    private String appUrl;
    private String role;
    private User user;

    public RoleChangeEvent(User user, String appUrl, String role) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
        this.role = role;
    }

    public User getUser() {
        return user;
    }
    public String getRole() {
        return role;
    }
    public String getAppUrl() {
        return appUrl;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }
}
