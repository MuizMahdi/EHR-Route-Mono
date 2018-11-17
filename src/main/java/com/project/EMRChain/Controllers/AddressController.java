package com.project.EMRChain.Controllers;
import com.project.EMRChain.Core.Utilities.AddressUtil;
import com.project.EMRChain.Core.Utilities.EcdsaUtil;
import com.project.EMRChain.Core.Utilities.StringUtil;
import com.project.EMRChain.Entities.Auth.User;
import com.project.EMRChain.Payload.Core.AddressResponse;
import com.project.EMRChain.Payload.Auth.ApiResponse;
import com.project.EMRChain.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;


@RestController
@RequestMapping("/address")
public class AddressController
{
    private UserService userService;
    private EcdsaUtil ecdsaUtil;
    private StringUtil stringUtil;
    private AddressUtil addressUtil;

    @Autowired
    public AddressController(UserService userService, EcdsaUtil ecdsaUtil, StringUtil stringUtil, AddressUtil addressUtil) {
        this.userService = userService;
        this.ecdsaUtil = ecdsaUtil;
        this.stringUtil = stringUtil;
        this.addressUtil = addressUtil;
    }


    @GetMapping("/generate")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> generateUserAddress(@RequestParam("username") String username)
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
        KeyPair keyPair = ecdsaUtil.ecGenerateKeyPair();
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey privKey = keyPair.getPrivate();

        String address = addressUtil.generateAddress(pubKey);
        String publicKey = stringUtil.getStringFromKey(pubKey);
        String privateKey = stringUtil.getStringFromKey(privKey);

        // Return address
        return new ResponseEntity<>(
                new AddressResponse(address, publicKey, privateKey),
                HttpStatus.OK
        );
    }

}
