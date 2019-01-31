package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Core.Utilities.AddressUtil;
import com.project.EhrRoute.Core.Utilities.KeyUtil;
import com.project.EhrRoute.Core.Utilities.RsaUtil;
import com.project.EhrRoute.Core.Utilities.StringUtil;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Payload.Core.AddressResponse;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Services.ProviderService;
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
    private ProviderService providerService;

    private RsaUtil rsaUtil;
    private KeyUtil keyUtil;
    private AddressUtil addressUtil;

    @Autowired
    public AddressController(UserService userService, ProviderService providerService, RsaUtil rsaUtil, KeyUtil keyUtil, AddressUtil addressUtil) {
        this.userService = userService;
        this.providerService = providerService;
        this.rsaUtil = rsaUtil;
        this.keyUtil = keyUtil;
        this.addressUtil = addressUtil;
    }


    @GetMapping("/generate")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> generateUserAddress(@RequestParam("username") String username) throws GeneralSecurityException
    {
        User user = userService.findUserByUsernameOrEmail(username);

        if (user == null)
        {
            return new ResponseEntity<>(
                new ApiResponse(false, "Invalid username"),
                HttpStatus.BAD_REQUEST
            );
        }

        // Not first time login (default is false)
        if (user.isNonFirstLogin())
        {
            return new ResponseEntity<>(
                new ApiResponse(false, "Not the first user login, user already has an address"),
                HttpStatus.BAD_REQUEST
            );
        }

        // Update user as NonFirstLogin
        user.setNonFirstLogin(true);
        userService.saveUser(user);

        // Generate address
        KeyPair keyPair = rsaUtil.rsaGenerateKeyPair();
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey privKey = keyPair.getPrivate();

        String address = addressUtil.generateAddress(pubKey);
        String publicKey = keyUtil.getStringFromPublicKey(pubKey);
        String privateKey = keyUtil.getStringFromPrivateKey(privKey);

        // Add address to provider details of user
        providerService.setProviderAddress(user, address);

        // Return address
        return new ResponseEntity<>(
            new AddressResponse(address, publicKey, privateKey),
            HttpStatus.OK
        );
    }

}
