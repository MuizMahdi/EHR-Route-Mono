package com.project.EhrRoute.Services;
import com.project.EhrRoute.Core.Block;
import com.project.EhrRoute.Entities.Core.ConsentRequestBlock;
import com.project.EhrRoute.Entities.Core.UpdateConsentRequest;
import com.project.EhrRoute.Entities.EHR.*;
import com.project.EhrRoute.Events.GetUserUpdateConsentEvent;
import com.project.EhrRoute.Payload.Core.BlockAddition;
import com.project.EhrRoute.Payload.Core.SerializableBlock;
import com.project.EhrRoute.Payload.Core.UserUpdateConsentResponse;
import com.project.EhrRoute.Payload.EHR.RecordUpdateData;
import com.project.EhrRoute.Utilities.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class TransactionService
{
    private ModelMapper modelMapper;
    private ApplicationEventPublisher eventPublisher;

    private UpdateConsentRequestService updateConsentRequestService;
    private ConsentRequestBlockService consentRequestService;

    private NotificationService notificationService;
    private EhrDetailService ehrDetailService;


    @Autowired
    public TransactionService(ModelMapper modelMapper, ApplicationEventPublisher eventPublisher, UpdateConsentRequestService updateConsentRequestService, ConsentRequestBlockService consentRequestService, NotificationService notificationService, EhrDetailService ehrDetailService) {
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
        this.updateConsentRequestService = updateConsentRequestService;
        this.consentRequestService = consentRequestService;
        this.notificationService = notificationService;
        this.ehrDetailService = ehrDetailService;
    }


    public void sendUpdateConsentRequest(BlockAddition updatedBlockAddition, RecordUpdateData recordUpdateData)
    {
        // Send a consent request for adding the block to chain to user
        // Generate and construct a block using data in block addition request
        Block block = modelMapper.mapAdditionRequestToBlock(updatedBlockAddition);

        // Convert the block into a serializable block
        SerializableBlock serializableBlock = modelMapper.mapBlockToSerializableBlock(block);

        // Construct and publish a GetUserUpdateConsent Event
        GetUserUpdateConsentEvent getUserConsent = new GetUserUpdateConsentEvent(
            this,
            recordUpdateData,
            serializableBlock,
            updatedBlockAddition.getProviderUUID(),
            updatedBlockAddition.getNetworkUUID(),
            updatedBlockAddition.getEhrUserID()
        );

        eventPublisher.publishEvent(getUserConsent);
    }


    @EventListener
    protected void getUserUpdateConsent(GetUserUpdateConsentEvent event)
    {
        Long userID = Long.parseLong(event.getUserID());

        /*
        *   Create and save an UpdateConsentRequest type notification for the user
        */

        // Create a consent request from block, user ID and provider UUID
        ConsentRequestBlock consentRequest = modelMapper.mapToConsentRequestBlock(
            userID,
            event.getProviderUUID(),
            event.getNetworkUUID(), // NetworkUUID of the provider
            event.getBlock()
        );

        // Persist the consent request
        consentRequestService.saveConsentRequest(consentRequest);

        /* Generate an EHR Detail and add the EHR proposed update data into it.
        This will be used as a temp EhrDetail until the user accepts or rejects the request. */
        EhrDetails ehrDetails = new EhrDetails();

        /* Get the EHR Data of the update consent request from the event */
        event.getRecordUpdateData().getConditions().forEach(medicalProblem -> {
            EhrProblems ehrProblem = new EhrProblems(medicalProblem);
            ehrDetails.addProblem(ehrProblem);
        });

        event.getRecordUpdateData().getAllergies().forEach(allergy -> {
            EhrAllergies ehrAllergy = new EhrAllergies(allergy);
            ehrDetails.addAllergy(ehrAllergy);
        });

        // Persist changes
        ehrDetailService.saveEhrDetails(ehrDetails);

        // Create and save an UpdateConsentRequest
        UpdateConsentRequest updateConsentRequest = updateConsentRequestService.createUpdateConsentRequest(consentRequest, ehrDetails);

        // Send a notification to the user
        notificationService.notifyUser(consentRequest, updateConsentRequest);
    }
}
