package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Models.UuidSourceType;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Payload.Core.*;
import com.project.EhrRoute.Services.*;
import com.project.EhrRoute.Utilities.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/transaction")
public class TransactionController
{
    private UuidUtil uuidUtil;
    private UserService userService;
    private TransactionService transactionService;


    @Autowired
    public TransactionController(UuidUtil uuidUtil, UserService userService, TransactionService transactionService) {
        this.uuidUtil = uuidUtil;
        this.userService = userService;
        this.transactionService = transactionService;
    }


    @PostMapping("/record-consent-request")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity getUserConsent(@RequestBody BlockAddition blockAddition)
    {
        transactionService.getUserConsent(blockAddition);
        return ResponseEntity.accepted().body(new ApiResponse(true, "Consent request was sent"));
    }


    @PostMapping("/record-consent-response")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity giveUserConsent(@RequestBody UserConsentResponse consentResponse) throws Exception
    {
        transactionService.giveUserConsent(consentResponse);
        return ResponseEntity.accepted().body(new ApiResponse(true, "Block has been signed and Broadcasted"));
    }


    @PostMapping("/record-update-consent-request")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity getUserUpdateConsent(@RequestBody UpdatedBlockAddition updatedBlockAddition)
    {
        transactionService.getUserUpdateConsent(updatedBlockAddition);
        return ResponseEntity.accepted().body("Update consent request was sent");
    }


    @PostMapping("/record-update-consent-response")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity giveUserUpdateConsent(@RequestBody UserUpdateConsentResponse updateConsentResponse) throws Exception
    {
        transactionService.giveUserUpdateConsent(updateConsentResponse);
        return ResponseEntity.accepted().body(new ApiResponse(true, "Updated block has been signed and Broadcasted successfully"));
    }
}
