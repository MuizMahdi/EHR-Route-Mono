package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Security.CurrentUser;
import com.project.EhrRoute.Security.UserPrincipal;
import com.project.EhrRoute.Services.ProviderService;
import com.project.EhrRoute.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequestMapping("providers")
public class ProviderController
{
    private UserService userService;
    private ProviderService providerService;

    @Autowired
    public ProviderController(UserService userService, ProviderService providerService) {
        this.userService = userService;
        this.providerService = providerService;
    }


    @GetMapping("/current/uuid")
    public ResponseEntity getCurrentProviderUUID(@CurrentUser UserPrincipal currentUser)
    {
        // Validate authentication
        if (currentUser == null) {
            return new ResponseEntity<>(
                new ApiResponse(false, "User not logged in"),
                HttpStatus.BAD_REQUEST
            );
        }

        // Validate user
        if (userService.isValidUserUsername(currentUser.getUsername())) {
            return new ResponseEntity<>(
                new ApiResponse(false, "User not found; Invalid user"),
                HttpStatus.BAD_REQUEST
            );
        }

        String providerUUID;

        // Get provider UUID
        try {
            providerUUID = providerService.getProviderUuidByUserID(currentUser.getId());
        }
        catch (ResourceNotFoundException Ex) {
            return new ResponseEntity<>(
                new ApiResponse(false, "User is not a provider"),
                HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(
            providerUUID,
            HttpStatus.OK
        );
    }


    @GetMapping("/current/address/exists")
    public ResponseEntity getCurrentProviderAddressExistence(@CurrentUser UserPrincipal currentUser)
    {
        // Validate authentication
        if (currentUser == null) {
            return new ResponseEntity<>(
                new ApiResponse(false, "User not logged in"),
                HttpStatus.BAD_REQUEST
            );
        }

        return ResponseEntity.ok(
            providerService.providerAddressExists(currentUser.getId())
        );
    }


    @GetMapping("/search-providers-by-username")
    public List<String> searchProvidersUsernamesByUsername(@RequestParam("keyword") String providerUsername)
    {
        return userService.searchProviderUsername(providerUsername);
    }
}
