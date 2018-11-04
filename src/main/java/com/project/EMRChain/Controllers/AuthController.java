package com.project.EMRChain.Controllers;
import com.project.EMRChain.Entities.Auth.Role;
import com.project.EMRChain.Entities.Auth.User;
import com.project.EMRChain.Exceptions.InternalErrorExcpetion;
import com.project.EMRChain.Models.RoleName;
import com.project.EMRChain.Payload.ApiResponse;
import com.project.EMRChain.Payload.JwtAuthenticationResponse;
import com.project.EMRChain.Payload.SignInRequest;
import com.project.EMRChain.Payload.SignUpRequest;
import com.project.EMRChain.Repositories.RoleRepository;
import com.project.EMRChain.Repositories.UserRepository;
import com.project.EMRChain.Security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
public class AuthController
{
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository)
    {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest signInRequest)
    {
        // Get User
        User user = userRepository.findByUsernameOrEmail(
                signInRequest.getUsernameOrEmail(),
                signInRequest.getUsernameOrEmail()
        ).orElse(null);

        if(user == null) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "User doesn't exist, wrong login credentials"),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (!user.isEnabled()) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "User didn't verify email"),
                    HttpStatus.UNAUTHORIZED
            );
        }

        // Auth
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getUsernameOrEmail(),
                        signInRequest.getPassword()
                )
        );

        // Set Auth in SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT from Auth
        String jwt = tokenProvider.generateJWT(authentication);

        // Return the JWT to client
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }


    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest)
    {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Create user from request data
        User user = new User(
                signUpRequest.getName(),
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword()
        );

        // Bcrypt hash the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Get the 'User' role
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() ->
                new InternalErrorExcpetion("User Role not set")
        );

        // Set the user role to 'User'
        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        System.out.println(location.toString());

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

}
