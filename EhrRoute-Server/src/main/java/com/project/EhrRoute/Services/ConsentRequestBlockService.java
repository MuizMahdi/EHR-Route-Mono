package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.Core.ConsentRequestBlock;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.project.EhrRoute.Repositories.ConsentRequestBlockRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
public class ConsentRequestBlockService
{
    private ConsentRequestBlockRepository consentRequestRepository;

    @Autowired
    public ConsentRequestBlockService(ConsentRequestBlockRepository consentRequestRepository) {
        this.consentRequestRepository = consentRequestRepository;
    }


    @Transactional
    public void checkConsentRequestExistence(ConsentRequestBlock consentRequest) {
        if (!consentRequestRepository.existsById(consentRequest.getId())) {
            throw new ResourceNotFoundException("ConsentRequestBlock", "ID", consentRequest.getId());
        }
    }


    @Transactional
    public void saveConsentRequest(ConsentRequestBlock consentRequest) {
        consentRequestRepository.save(consentRequest);
    }


    @Transactional
    public List<ConsentRequestBlock> findRequestsByProvider(String providerUUID)
    {
        List<ConsentRequestBlock> providerRequests = consentRequestRepository.findByProviderUUID(providerUUID);

        if (providerRequests.isEmpty() || providerRequests.size() < 1) {
            return null;
        }

        return providerRequests;
    }


    @Transactional
    public void deleteRequest(ConsentRequestBlock consentRequest) {
        consentRequestRepository.delete(consentRequest);
    }
}
