package com.project.EMRChain.Controllers;
import com.project.EMRChain.Entities.Auth.Role;
import com.project.EMRChain.Entities.Auth.User;
import com.project.EMRChain.Entities.Auth.VerificationToken;
import com.project.EMRChain.Events.OnRegistrationCompleteEvent;
import com.project.EMRChain.Exceptions.InternalErrorExcpetion;
import com.project.EMRChain.Models.RoleName;
import com.project.EMRChain.Payload.ApiResponse;
import com.project.EMRChain.Payload.JwtAuthenticationResponse;
import com.project.EMRChain.Payload.SignInRequest;
import com.project.EMRChain.Payload.SignUpRequest;
import com.project.EMRChain.Repositories.RoleRepository;
import com.project.EMRChain.Repositories.UserRepository;
import com.project.EMRChain.Security.JwtTokenProvider;
import com.project.EMRChain.Services.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.net.URI;
import java.util.Calendar;
import java.util.Collections;


@RestController
@RequestMapping("/user")
public class AuthController
{
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private ApplicationEventPublisher eventPublisher;
    private VerificationTokenService verificationTokenService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, ApplicationEventPublisher eventPublisher, VerificationTokenService verificationTokenService)
    {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.eventPublisher = eventPublisher;
        this.verificationTokenService = verificationTokenService;
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

        // Send a verification token to the user's email
        String appUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, appUrl));

        URI location = ServletUriComponentsBuilder
        .fromCurrentContextPath().path("/users/{username}")
        .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }


    @RequestMapping("/registration-confirm/{verificationToken}")
    public ResponseEntity<ApiResponse> confirmRegistration(@PathVariable("verificationToken") String token)
    {
        // Get current calendar time
        Calendar cal = Calendar.getInstance();

        // Get token object from DB using the token string taken from url.
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);

        // Invalid token
        if (verificationToken == null)
        {
            return new ResponseEntity<>(
                new ApiResponse(false, "Invalid or expired email verification link "),
                HttpStatus.BAD_REQUEST
            );

        }

        // Expired token
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0)
        {
            return new ResponseEntity<>(
                new ApiResponse(false, "Expired email verification link"),
                HttpStatus.BAD_REQUEST
            );

        }

        User user = verificationToken.getUser(); // Get the token's user

        // Enable user account
        user.setEnabled(true);

        // Update user isEnabled on DB
        userRepository.save(user);

        return new ResponseEntity<>(
                new ApiResponse(true, "User account verified successfully"),
                HttpStatus.OK
        );
    }

}
