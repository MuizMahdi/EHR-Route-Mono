package com.project.EhrRoute.Events;
import com.project.EhrRoute.Entities.Auth.User;
import org.springframework.context.ApplicationEvent;
import java.util.Locale;


public class RegistrationCompleteEvent extends ApplicationEvent
{
    private String appUrl;
    private Locale locale;
    private User user;


    public RegistrationCompleteEvent(User user, String appUrl)
    {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }


    public User getUser() {
        return user;
    }
    public String getAppUrl() {
        return appUrl;
    }
    public Locale getLocale() {
        return locale;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
