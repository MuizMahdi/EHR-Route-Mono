package com.project.EhrRoute;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Entities.Core.ChainRoot;
import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class EhrRouteApplication
{
    @PostConstruct
    void onInit()
    {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }


    public static void main(String[] args) {
        SpringApplication.run(EhrRouteApplication.class, args);
    }

}