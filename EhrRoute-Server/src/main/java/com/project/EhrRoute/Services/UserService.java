package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.Auth.Role;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Exceptions.InternalErrorException;
import com.project.EhrRoute.Models.RoleName;
import com.project.EhrRoute.Payload.Auth.SignUpRequest;
import com.project.EhrRoute.Repositories.RoleRepository;
import com.project.EhrRoute.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
                new InternalErrorException("User Role not set")
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

    @Transactional
    public User findUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        return user;
    }
}
