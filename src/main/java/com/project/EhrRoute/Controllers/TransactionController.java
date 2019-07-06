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


    @PostMapping("/get-update-consent")
    @PreAuthorize("hasRole('PROVIDER')")
    public ResponseEntity getUserUpdateConsent(@RequestBody UpdatedBlockAddition updatedBlockAddition)
    {
        String providerUUID = updatedBlockAddition.getBlockAddition().getProviderUUID();
        String networkUUID = updatedBlockAddition.getBlockAddition().getNetworkUUID();
        String userID = updatedBlockAddition.getBlockAddition().getEhrUserID();

        uuidUtil.validateResourceUUID(providerUUID, UuidSourceType.PROVIDER);
        uuidUtil.validateResourceUUID(networkUUID, UuidSourceType.NETWORK);

        // Validate user
        userService.findUserById(Long.parseLong(userID));

        // Send a notification
        transactionService.sendUpdateConsentRequest(updatedBlockAddition.getBlockAddition(), updatedBlockAddition.getRecordUpdateData());

        return ResponseEntity.accepted().build();
    }


    @PostMapping("/give-update-consent")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity giveUserUpdateConsent(@RequestBody UserUpdateConsentResponse updateConsentResponse) throws Exception
    {
        /*
                // Validate the consent request that the user responded to
                if (!consentRequestService.isConsentResponseValid(updateConsentResponse.getConsentResponse())) {
                    return ResponseEntity.badRequest().body(new ApiResponse(false, "Provider has not made a consent request for this response"));
                }

                // Get Block from SerializableBlock in the consent request of the update consent response
                Block block = modelMapper.mapSerializableBlockToBlock(updateConsentResponse.getConsentResponse().getBlock());

                // Sign and update the block.
                block = signBlock(updateConsentResponse.getConsentResponse(), block);
                block = updateBlockLeafHash(block);

                // Construct a block addition response
                BlockResponse blockResponse = new BlockResponse(
                    modelMapper.mapBlockToSerializableBlock(block),
                    new BlockMetadata(updateConsentResponse.getConsentResponse().getConsentRequestUUID(), BlockSource.BROADCAST.toString())
                );

                // Broadcast the signed block to the other provider nodes in network.
                blockTransmitter.broadcast(blockResponse);
        */
        return ResponseEntity.accepted().body(new ApiResponse(true, "Updated block has been signed and Broadcasted successfully"));
    }
}
