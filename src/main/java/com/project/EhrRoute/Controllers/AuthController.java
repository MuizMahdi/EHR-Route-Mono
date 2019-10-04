package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Entities.Auth.Role;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Entities.Auth.VerificationToken;
import com.project.EhrRoute.Events.RegistrationCompleteEvent;
import com.project.EhrRoute.Events.RoleChangeEvent;
import com.project.EhrRoute.Exceptions.InternalErrorException;
import com.project.EhrRoute.Models.RoleName;
import com.project.EhrRoute.Payload.Auth.*;
import com.project.EhrRoute.Repositories.RoleRepository;
import com.project.EhrRoute.Security.CurrentUser;
import com.project.EhrRoute.Security.JwtTokenProvider;
import com.project.EhrRoute.Security.UserPrincipal;
import com.project.EhrRoute.Services.ProviderService;
import com.project.EhrRoute.Services.UserService;
import com.project.EhrRoute.Services.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.Calendar;
import java.util.Set;


@RestController
@RequestMapping("/auth")
public class AuthController
{
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private ApplicationEventPublisher eventPublisher;
    private VerificationTokenService verificationTokenService;
    private UserService userService;
    private RoleRepository roleRepository;
    private ProviderService providerService;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, ApplicationEventPublisher eventPublisher, VerificationTokenService verificationTokenService, RoleRepository roleRepository, ProviderService providerService)
    {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.eventPublisher = eventPublisher;
        this.verificationTokenService = verificationTokenService;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.providerService = providerService;
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest signInRequest)
    {
        // Get User
        User user = userService.findUserByAddressOrEmail(signInRequest.getAddressOrEmail());

        if (!user.isEnabled()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "User didn't verify email"));
        }

        // Auth
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                signInRequest.getAddressOrEmail(),
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
        if(userService.userEmailExists(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email Address already in use"));
        }

        User user = userService.createUser(signUpRequest);

        // Send a verification token to the user's email
        String appUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();
        eventPublisher.publishEvent(new RegistrationCompleteEvent(user, appUrl));

        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
    }


    @RequestMapping("/registration-confirm/{verificationToken}")
    public ResponseEntity<ApiResponse> confirmRegistration(@NotBlank @PathVariable("verificationToken") String token)
    {
        // Get current calendar time
        Calendar cal = Calendar.getInstance();

        // Get token object from DB using the token string taken from url.
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);

        // Invalid token
        if (verificationToken == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid or expired email verification link"));
        }

        // Expired token
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Expired email verification link"));
        }

        User user = verificationToken.getUser(); // Get the token's user

        // Enable user account
        user.setEnabled(true);

        // Update user isEnabled on DB
        userService.saveUser(user);

        return ResponseEntity.ok(new ApiResponse(true, "User account verified successfully"));
    }


    @PostMapping("/user-role-change")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROVIDER')")
    public ResponseEntity<?> roleChangeToken(@Valid @RequestBody RoleChangeRequest roleChangeRequest, @CurrentUser UserPrincipal currentUser)
    {
        // User who's role is going to be changed
        User user = userService.findUserByAddress(roleChangeRequest.getAddress());

        // Get current app url
        String appUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();

        // Role to be added
        String role = roleChangeRequest.getRole();

        // Get the institution name of the current provider, the user who's role is going to change will have this institution
        String providerInstitution = providerService.getProviderInstitution(currentUser.getId());

        // Send a role change token to the user's email
        eventPublisher.publishEvent(new RoleChangeEvent(user, appUrl, role, providerInstitution));

        return ResponseEntity.ok(new ApiResponse(true, "Role change token sent successfully"));
    }


    @RequestMapping("/role-change/{verificationToken}")
    public ResponseEntity<ApiResponse> roleChange(@PathVariable("verificationToken") String token, @RequestParam("role") String role, @RequestParam("institution") String institutionName)
    {
        // Check if role is a valid role
        if (!isRoleValid(role)) return ResponseEntity.notFound().build();

        // Get current calendar time
        Calendar cal = Calendar.getInstance();

        // Get token object from DB using the token string taken from url.
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);

        // Invalid token
        if (verificationToken == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid role change token"));
        }

        // Expired token
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Expired role change token"));
        }

        // Get the token's user
        User user = verificationToken.getUser();

        // Get the role
        Role userRole = roleRepository.findByName(RoleName.valueOf(role)).orElseThrow(() ->
            new InternalErrorException("Invalid Role")
        );

        // Update the user roles set
        Set<Role> userRoles = user.getRoles();
        userRoles.add(userRole);
        user.setRoles(userRoles);

        // Create provider details for user, then find institution by name and set it as this user's institution
        providerService.generateUserProviderDetails(user, institutionName);

        // Persist the user updates to DB
        userService.saveUser(user);

        return ResponseEntity.ok(new ApiResponse(true, "User Role Was Added Successfully."));
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
