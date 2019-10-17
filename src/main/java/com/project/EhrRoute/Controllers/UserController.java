package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Entities.Auth.Role;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Exceptions.InternalErrorException;
import com.project.EhrRoute.Exceptions.NullUserNetworkException;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import com.project.EhrRoute.Models.RoleName;
import com.project.EhrRoute.Payload.App.ProviderAdditionRequest;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Payload.Auth.UserRoleResponse;
import com.project.EhrRoute.Payload.Core.UserNetworksResponse;
import com.project.EhrRoute.Repositories.RoleRepository;
import com.project.EhrRoute.Security.CurrentUser;
import com.project.EhrRoute.Security.UserPrincipal;
import com.project.EhrRoute.Services.ProviderService;
import com.project.EhrRoute.Services.UserService;
import com.project.EhrRoute.Utilities.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/users")
public class UserController
{
    private UserService userService;
    private RoleRepository roleRepository;
    private ProviderService providerService;
    private ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, RoleRepository roleRepository, ProviderService providerService) {
        this.userService = userService;
        this.providerService = providerService;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/current")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PROVIDER')")
    public ResponseEntity getCurrentUserInfo(@CurrentUser UserPrincipal currentUser)
    {
        return ResponseEntity.ok(userService.getUserInfo(currentUser.getId()));
    }


    @GetMapping("/current/roles")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PROVIDER')")
    public ResponseEntity<?> getCurrentUserRoles(@CurrentUser UserPrincipal currentUser)
    {
        Set<Role> userRoles;

        // Get the user's set of Role
        userRoles = userService.findUserRoles(currentUser.getId());

        // Create a set of UserRoleResponse 
        Set<UserRoleResponse> userRolesNames = new HashSet<>();

        // Map each user Role's role name to value in userRoleNames set
        for (Role role : userRoles) {
            UserRoleResponse userRole = new UserRoleResponse(role.getName().toString());
            userRolesNames.add(userRole);
        }

        // Return the userRoleNames set
        return ResponseEntity.ok(userRolesNames);
    }


    @GetMapping("/current/networks")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity getCurrentUserNetwork(@CurrentUser UserPrincipal currentUser)
    {
        User user = userService.findUserById(currentUser.getId());

        Set<Network> userNetworks;

        try {
            userNetworks = userService.findUserNetworks(user);
        }
        catch (NullUserNetworkException Ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, Ex.getMessage()));
        }

        // Map Networks set to UserNetworksResponse payload
        UserNetworksResponse userNetworksResponse = modelMapper.mapNetworksToUserNetworksResponse(userNetworks);

        return ResponseEntity.ok(userNetworksResponse);
    }


    @GetMapping("/current/first-login-status")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('PROVIDER')")
    public ResponseEntity getCurrentUserFirstLoginStatus(@CurrentUser UserPrincipal currentUser)
    {
        boolean isFirstLogin = userService.isUserFirstLogin(currentUser.getId());
        return ResponseEntity.ok(isFirstLogin);
    }


    @PostMapping("/current/info-addition-status")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity setCurrentUserHasAddedInfoStatus(@CurrentUser UserPrincipal currentUser)
    {
        userService.setUserHasAddedInfo(currentUser.getId());
        return ResponseEntity.ok(new ApiResponse(false, "Your info has been saved"));
    }


    @GetMapping("/search-by-address")
    public List<String> searchUsersnamesByAddress(@RequestParam("keyword") String address)
    {
        return userService.searchAddress(address);
    }


    @GetMapping("/{address}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROVIDER')")
    public ResponseEntity getUserByUsername(@PathVariable("address") String address)
    {
        User user = userService.findUserByAddress(address);
        return ResponseEntity.ok (userService.getUserInfo(user.getId()));
    }


    @PostMapping("/providers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity addProvider(@Valid @RequestBody ProviderAdditionRequest providerAdditionRequest)
    {
        /* Get and validate user with username */
        User user = userService.findUserByAddress(providerAdditionRequest.getAddress());

        /* Set user's role to Provider */
        // Get the role
        Role userRole = roleRepository.findByName(RoleName.ROLE_PROVIDER).orElseThrow(() ->
            new InternalErrorException("Invalid Role")
        );

        // Update the user roles set
        Set<Role> userRoles = user.getRoles();
        userRoles.add(userRole);
        user.setRoles(userRoles);

        // Persist changes of user roles
        userService.saveUser(user);

        // Create an institution and generate provider details for user
        providerService.generateInstitutionProviderDetails(user, providerAdditionRequest.getInstitutionName());

        return ResponseEntity.ok(new ApiResponse(true, "Provider has been successfully added"));
    }
}
