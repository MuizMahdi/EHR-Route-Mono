package com.project.EMRChain;
import com.project.EMRChain.Entities.Auth.Role;
import com.project.EMRChain.Models.RoleName;
import com.project.EMRChain.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class EmrChainApplication
{

    @PostConstruct
    void onInit() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }


	public static void main(String[] args) {
		SpringApplication.run(EmrChainApplication.class, args);
	}

}
