package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Core.Utilities.AddressUtil;
import com.project.EhrRoute.Core.Utilities.KeyUtil;
import com.project.EhrRoute.Core.Utilities.RsaUtil;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Payload.Core.AddressResponse;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Security.CurrentUser;
import com.project.EhrRoute.Security.UserPrincipal;
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

    private RsaUtil rsaUtil;
    private KeyUtil keyUtil;
    private AddressUtil addressUtil;

    @Autowired
    public AddressController(UserService userService, RsaUtil rsaUtil, KeyUtil keyUtil, AddressUtil addressUtil) {
        this.userService = userService;
        this.rsaUtil = rsaUtil;
        this.keyUtil = keyUtil;
        this.addressUtil = addressUtil;
    }


    @GetMapping("/generate")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> generateUserAddress(@CurrentUser UserPrincipal currentUser) throws GeneralSecurityException
    {
        User user = userService.findUserByUsernameOrEmail(currentUser.getUsername());

        if (user == null)
        {
            return new ResponseEntity<>(
                new ApiResponse(false, "User not logged in. Invalid user"),
                HttpStatus.BAD_REQUEST
            );
        }

        // Not first time login (default is false)
        if (!user.isFirstLogin())
        {
            return new ResponseEntity<>(
                new ApiResponse(false, "Not the user's first login, user already has an address"),
                HttpStatus.CONFLICT
            );
        }

        // Update user as non-first login
        user.setFirstLogin(false);
        userService.saveUser(user);

        // Generate address
        KeyPair keyPair = rsaUtil.rsaGenerateKeyPair();
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey privKey = keyPair.getPrivate();

        String address = addressUtil.generateAddress(pubKey);
        String publicKey = keyUtil.getStringFromPublicKey(pubKey);
        String privateKey = keyUtil.getStringFromPrivateKey(privKey);

        // Return address
        return new ResponseEntity<>(
            new AddressResponse(address, publicKey, privateKey),
            HttpStatus.OK
        );
    }

}
