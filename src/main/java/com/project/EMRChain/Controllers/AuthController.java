package com.project.EMRChain.Controllers;
import com.project.EMRChain.Entities.Auth.Role;
import com.project.EMRChain.Entities.Auth.User;
import com.project.EMRChain.Entities.Auth.VerificationToken;
import com.project.EMRChain.Events.OnRegistrationCompleteEvent;
import com.project.EMRChain.Models.RoleName;
import com.project.EMRChain.Payload.Auth.ApiResponse;
import com.project.EMRChain.Payload.Auth.JwtAuthenticationResponse;
import com.project.EMRChain.Payload.Auth.SignInRequest;
import com.project.EMRChain.Payload.Auth.SignUpRequest;
import com.project.EMRChain.Security.JwtTokenProvider;
import com.project.EMRChain.Services.UserService;
import com.project.EMRChain.Services.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.net.URI;
import java.util.Calendar;


@RestController
@RequestMapping("/auth")
public class AuthController
{
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private ApplicationEventPublisher eventPublisher;
    private VerificationTokenService verificationTokenService;
    private UserService userService;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, ApplicationEventPublisher eventPublisher, VerificationTokenService verificationTokenService)
    {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.eventPublisher = eventPublisher;
        this.verificationTokenService = verificationTokenService;
        this.userService = userService;
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest signInRequest)
    {
        // Get User
        User user = userService.findUserByUsernameOrEmail(signInRequest.getUsernameOrEmail());

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
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest)
    {
        if(userService.userEmailExists(signUpRequest.getEmail()))
        {
            return new ResponseEntity<>(
                new ApiResponse(false, "Email Address already in use!"),
                HttpStatus.BAD_REQUEST
            );
        }

        User user = userService.createUser(signUpRequest);

        // Send a verification token to the user's email
        String appUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, appUrl));

        URI location = ServletUriComponentsBuilder
        .fromCurrentContextPath().path("/users/{username}")
        .buildAndExpand(user.getUsername()).toUri();

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
        userService.saveUser(user);

        return new ResponseEntity<>(
                new ApiResponse(true, "User account verified successfully"),
                HttpStatus.OK
        );
    }

    @RequestMapping("/role-change/{role}/{verificationToken}")
    public ResponseEntity<ApiResponse> roleChange(@PathVariable("role") String role, @PathVariable("verificationToken") String token)
    {
        // Check if role is a valid role
        if (!isRoleValid(role)) {
            return new ResponseEntity<>(
                new ApiResponse(false, "Invalid Role"),
                HttpStatus.BAD_REQUEST
            );
        }

        // Get current calendar time
        Calendar cal = Calendar.getInstance();

        // Get token object from DB using the token string taken from url.
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);

        // Invalid token
        if (verificationToken == null) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Invalid or expired role change token"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Expired token
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Expired role change token"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Get the token's user
        User user = verificationToken.getUser();

        // Create Role from 'role' String
        Role userRole = new Role();
        userRole.setName(RoleName.valueOf(role));

        // Add it to user roles
        user.getRoles().add(userRole);

        // Update user data with the new role on DB
        userService.saveUser(user);

        return new ResponseEntity<>(
                new ApiResponse(true, "User account verified successfully"),
                HttpStatus.OK
        );
    }

    private boolean isRoleValid(String role)
    {
        for (RoleName roleName : RoleName.values())
        {
            if(roleName.toString().equals(role)) {
                return true;
            }
        }

        return false;
    }

}
