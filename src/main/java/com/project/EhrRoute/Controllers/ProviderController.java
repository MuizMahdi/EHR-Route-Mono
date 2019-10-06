package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import com.project.EhrRoute.Payload.App.SimpleStringPayload;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Security.CurrentUser;
import com.project.EhrRoute.Security.UserPrincipal;
import com.project.EhrRoute.Services.ProviderService;
import com.project.EhrRoute.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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

        return ResponseEntity.ok(
            new SimpleStringPayload(providerUUID)
        );
    }


    @PostMapping("/current/address")
    public ResponseEntity setCurrentProviderAddress(@RequestBody @Valid @NotBlank SimpleStringPayload address, @CurrentUser UserPrincipal currentUser)
    {
        User user = userService.findUserById(currentUser.getId());
        providerService.setProviderAddress(user, address.getPayload());
        return ResponseEntity.ok(new ApiResponse(true, "Address was saved successfully"));
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


    @GetMapping("/search-providers-by-address")
    public List<String> searchProvidersUsernamesByUsername(@RequestParam("keyword") String providerAddress)
    {
        return userService.searchProvider(providerAddress);
    }
}
