package com.project.EhrRoute.Events;
import com.project.EhrRoute.Entities.Auth.User;
import org.springframework.context.ApplicationEvent;


public class RoleChangeEvent extends ApplicationEvent
{
    private String appUrl;
    private String role;
    private String institution;
    private User user;


    public RoleChangeEvent(User user, String appUrl, String role, String institution) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
        this.role = role;
        this.institution = institution;
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
    public String getInstitution() {
        return institution;
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
    public void setInstitution(String institution) {
        this.institution = institution;
    }
}
