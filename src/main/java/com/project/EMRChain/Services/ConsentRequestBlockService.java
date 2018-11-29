package com.project.EMRChain.Services;
import com.project.EMRChain.Entities.Core.ConsentRequestBlock;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.project.EMRChain.Repositories.ConsentRequestBlockRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public void saveConsentRequest(ConsentRequestBlock block)
    {
        consentRequestRepository.save(block);
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
}
