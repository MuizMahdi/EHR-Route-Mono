package com.project.EhrRoute.Controllers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Payload.Core.*;
import com.project.EhrRoute.Payload.EHR.TempEhrHistory;
import com.project.EhrRoute.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/transaction")
public class TransactionController
{
    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @PostMapping("/record-consent-request")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity getUserConsent(@RequestBody BlockAddition blockAddition) {
        transactionService.getUserConsent(blockAddition);
        return ResponseEntity.accepted().body(new ApiResponse(true, "Consent request was sent"));
    }


    @PostMapping("/record-consent-response")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity giveUserConsent(@RequestBody UserConsentResponse consentResponse) throws Exception {
        // TODO: TEMP, FIX THE SERIALIZATION ISSUE
        UserConsentResponse modifiedConsentResponse = createEhrHistoryMap(consentResponse);
        // TODO: TEMP, FIX THE SERIALIZATION ISSUE
        transactionService.giveUserConsent(modifiedConsentResponse);
        return ResponseEntity.accepted().body(new ApiResponse(true, "Block has been signed and Broadcasted"));
    }

    @PostMapping("/record-update-consent-request")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity getUserUpdateConsent(@RequestBody UpdatedBlockAddition updatedBlockAddition) {
        transactionService.getUserUpdateConsent(updatedBlockAddition);
        return ResponseEntity.accepted().body("Update consent request was sent");
    }


    @PostMapping("/record-update-consent-response")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity giveUserUpdateConsent(@RequestBody UserUpdateConsentResponse updateConsentResponse) throws Exception {
        // TODO: TEMP, FIX THE SERIALIZATION ISSUE
        UserConsentResponse modifiedConsentResponse = createEhrHistoryMap(updateConsentResponse.getConsentResponse());
        updateConsentResponse.setConsentResponse(modifiedConsentResponse);
        // TODO: TEMP, FIX THE SERIALIZATION ISSUE
        transactionService.giveUserUpdateConsent(updateConsentResponse);
        return ResponseEntity.accepted().body(new ApiResponse(true, "Updated block has been signed and Broadcasted successfully"));
    }

    private UserConsentResponse createEhrHistoryMap(UserConsentResponse consentResponse) {
        HashMap<String, Boolean> history = new HashMap<>();
        List<TempEhrHistory> ehrHistory = consentResponse.getMedicalHistory();
        UserConsentResponse modifiedConsentResponse = consentResponse;

        for (TempEhrHistory historyEntry : ehrHistory) {
            history.put(historyEntry.getCondition(), historyEntry.getOccurrence());
        }

        modifiedConsentResponse.getBlock().getTransaction().getRecord().setHistory(history);
        return modifiedConsentResponse;
    }
}
