package com.project.EMRChain.Services;
import com.project.EMRChain.Entities.Core.ConsentRequestBlock;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.project.EMRChain.Repositories.ConsentRequestBlockRepository;
import org.springframework.transaction.annotation.Transactional;


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
}
