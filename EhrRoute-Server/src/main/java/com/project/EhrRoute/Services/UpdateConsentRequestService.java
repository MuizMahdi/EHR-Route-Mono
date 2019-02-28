package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.Core.ConsentRequestBlock;
import com.project.EhrRoute.Entities.Core.UpdateConsentRequest;
import com.project.EhrRoute.Entities.EHR.EhrDetails;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import com.project.EhrRoute.Repositories.UpdateConsentRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UpdateConsentRequestService
{
    private UpdateConsentRequestRepository updateConsentRequestRepository;
    private ConsentRequestBlockService consentRequestBlockService;
    private EhrDetailService ehrDetailService;

    @Autowired
    public UpdateConsentRequestService(UpdateConsentRequestRepository updateConsentRequestRepository, ConsentRequestBlockService consentRequestBlockService, EhrDetailService ehrDetailService) {
        this.updateConsentRequestRepository = updateConsentRequestRepository;
        this.consentRequestBlockService = consentRequestBlockService;
        this.ehrDetailService = ehrDetailService;
    }


    @Transactional
    public UpdateConsentRequest createUpdateConsentRequest(ConsentRequestBlock consentRequestBlock, EhrDetails ehrDetails)
    {
        // Validate Consent Request Block
        consentRequestBlockService.checkConsentRequestExistence(consentRequestBlock);

        // Validate EHR Details
        ehrDetailService.checkEhrDetailsExistence(ehrDetails);

        // Create an UpdateConsentRequest
        UpdateConsentRequest updateConsentRequest = new UpdateConsentRequest(consentRequestBlock, ehrDetails);

        // Persist the request
        return updateConsentRequestRepository.save(updateConsentRequest);
    }
}
