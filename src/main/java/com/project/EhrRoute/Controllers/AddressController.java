package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Core.Utilities.AddressUtil;
import com.project.EhrRoute.Core.Utilities.KeyUtil;
import com.project.EhrRoute.Core.Utilities.RsaUtil;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Models.RoleName;
import com.project.EhrRoute.Payload.Core.AddressResponse;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Security.CurrentUser;
import com.project.EhrRoute.Security.UserPrincipal;
import com.project.EhrRoute.Services.EhrDetailService;
import com.project.EhrRoute.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;


@RestController
@RequestMapping("/address")
public class AddressController
{
    private UserService userService;
    private EhrDetailService ehrDetailService;

    private RsaUtil rsaUtil;
    private KeyUtil keyUtil;
    private AddressUtil addressUtil;

    @Autowired
    public AddressController(UserService userService, EhrDetailService ehrDetailService, RsaUtil rsaUtil, KeyUtil keyUtil, AddressUtil addressUtil) {
        this.userService = userService;
        this.ehrDetailService = ehrDetailService;
        this.rsaUtil = rsaUtil;
        this.keyUtil = keyUtil;
        this.addressUtil = addressUtil;
    }


    @GetMapping("/generate")
    @PreAuthorize("hasRole('USER') or hasRole('PROVIDER')")
    public ResponseEntity<?> generateUserAddress(@CurrentUser UserPrincipal currentUser) throws GeneralSecurityException
    {
        // Get user
        User user = userService.findUserByUsernameOrEmail(currentUser.getUsername());

        // Validate user
        if (user == null) {
            return new ResponseEntity<>(
                new ApiResponse(false, "User not logged in. Invalid user"),
                HttpStatus.BAD_REQUEST
            );
        }

        // If its not the user's first time login
        if (!user.isFirstLogin()) {
            return new ResponseEntity<>(
                new ApiResponse(false, "Not the user's first login, user already has an address"),
                HttpStatus.CONFLICT
            );
        }

        // Update user as non-first login
        user.setFirstLogin(false);
        userService.saveUser(user);

        // Generate keypair
        KeyPair keyPair = rsaUtil.rsaGenerateKeyPair();
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey privKey = keyPair.getPrivate();

        // Generate address from public key
        String address = addressUtil.generateAddress(pubKey);

        // Get base64 encoded keys
        String publicKey = keyUtil.getStringFromPublicKey(pubKey);
        String privateKey = keyUtil.getStringFromPrivateKey(privKey);

        /*
        // Check if user doesn't have a 'Provider' or 'Admin' role
        if (!userService.userHasRole(user, RoleName.ROLE_PROVIDER) && !userService.userHasRole(user, RoleName.ROLE_ADMIN)) {
            // Generate an EHR detail using user's address
            ehrDetailService.generateUserEhrDetails(address);
        }
        */

        // Return Address Response
        return new ResponseEntity<>(
            new AddressResponse(address, publicKey, privateKey),
            HttpStatus.OK
        );
    }

}
