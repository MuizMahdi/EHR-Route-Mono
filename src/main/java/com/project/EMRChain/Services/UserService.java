package com.project.EMRChain.Services;
import com.project.EMRChain.Entities.Auth.Role;
import com.project.EMRChain.Entities.Auth.User;
import com.project.EMRChain.Events.RoleChangeEvent;
import com.project.EMRChain.Exceptions.InternalErrorExcpetion;
import com.project.EMRChain.Models.RoleName;
import com.project.EMRChain.Payload.Auth.SignUpRequest;
import com.project.EMRChain.Repositories.RoleRepository;
import com.project.EMRChain.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserService
{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Transactional
    public boolean userEmailExists(String email)
    {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User createUser(SignUpRequest request) {
        // Create user from request data
        User user = new User(
                request.getName(),
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );

        // Bcrypt hash the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Get the 'User' role
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() ->
                new InternalErrorExcpetion("User Role not set")
        );

        // Set the user role to 'User'
        user.setRoles(Collections.singleton(userRole));

        // Save user in DB
        User result = userRepository.save(user);

        return result;
    }

    @Transactional
    public void saveUser(User user)
    {
        userRepository.save(user);
    }

    @Transactional
    public User findUserByUsernameOrEmail(String usernameOrEmail) {
        User user = userRepository.findByUsernameOrEmail(
                usernameOrEmail, usernameOrEmail
        ).orElse(null);

        return user;
    }
}
