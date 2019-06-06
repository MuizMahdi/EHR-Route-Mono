package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.Core.ConsentRequestBlock;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import com.project.EhrRoute.Payload.Core.UserConsentResponse;
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
    public void consentRequestExists(ConsentRequestBlock consentRequest) {
        if (!consentRequestRepository.existsById(consentRequest.getId())) {
            throw new ResourceNotFoundException("ConsentRequestBlock", "ID", consentRequest.getId());
        }
    }


    @Transactional
    public void saveConsentRequest(ConsentRequestBlock consentRequest) {
        consentRequestRepository.save(consentRequest);
    }


    /**
     * Finds a consent request with consent request UUID
     * @param consentRequestUUID            the UUID of the consent request generated on request creation
     * @return                              the ConsentRequestBlock that has the UUID
     * @throws ResourceNotFoundException    exception thrown when a request with the UUID not found
     */
    @Transactional
    public ConsentRequestBlock findConsentRequest(String consentRequestUUID) {
        // return the consent request block using the UUID
        return consentRequestRepository.findByRequestUUID(consentRequestUUID).orElseThrow(() -> {
            return new ResourceNotFoundException("Consent Request", "UUID", consentRequestUUID);
        });
    }


    /**
     * Finds the respective consent request for a consent response
     * @param consentResponse   the user's response for a consent request made by a provider
     * @return                  the request that the provider has made
     */
    public ConsentRequestBlock findMatchingConsentRequest(UserConsentResponse consentResponse) {
        // Consent request UUID of the response
        String consentRequestUUID = consentResponse.getConsentRequestUUID();

        // return the consent request block using the UUID
        return consentRequestRepository.findByRequestUUID(consentRequestUUID).orElse(null);
    }


    /**
     * Validates consent response by checking if it has respective consent request
     * @param consentResponse   the user's response for a consent request made by a provider
     * @return                  boolean representation of the consent response existence
     */
    public boolean isConsentResponseValid(UserConsentResponse consentResponse) {
        return (findMatchingConsentRequest(consentResponse) != null);
    }


    @Transactional
    public void deleteRequest(ConsentRequestBlock consentRequest) {
        consentRequestRepository.delete(consentRequest);
    }
}
