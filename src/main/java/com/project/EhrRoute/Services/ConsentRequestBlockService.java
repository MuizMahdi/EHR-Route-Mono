package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.Core.ConsentRequestBlock;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import com.project.EhrRoute.Payload.Core.UserConsentResponse;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.project.EhrRoute.Repositories.ConsentRequestBlockRepository;
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
    public List<ConsentRequestBlock> findRequestsByProvider(String providerUUID) {
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


    public void deleteMatchingConsentRequest(UserConsentResponse consentResponse) {
        ConsentRequestBlock consentRequest = findMatchingConsentRequest(consentResponse);

        if (consentRequest != null) {
            deleteRequest(consentRequest);
        }
    }


    public boolean isConsentResponseValid(UserConsentResponse consentResponse) {
        return (findMatchingConsentRequest(consentResponse) != null);
    }

    /**
     * Finds the respective consent request for a consent response
     * @param consentResponse   the user's response for a consent request made by a provider
     * @return                  the request that the provider has made
     */
    public ConsentRequestBlock findMatchingConsentRequest(UserConsentResponse consentResponse) {

        // The UUID of the provider that made the request
        String responseProviderUUID = consentResponse.getProviderUUID();

        // Get the list of Consent requests made by that provider
        List<ConsentRequestBlock> providerConsentRequestsList = findRequestsByProvider(responseProviderUUID);

        // If the provider has any open(unanswered) consent requests
        if ((providerConsentRequestsList != null) && (!providerConsentRequestsList.isEmpty())) {

            // Create an array list from the provider's requests
            ArrayList<ConsentRequestBlock> providerConsentRequests = new ArrayList<>(providerConsentRequestsList);

            String responseTransactionId = consentResponse.getBlock().getTransaction().getTransactionId();

            // if a request that has transactionId identical to responseTransactionId is found in providerConsentRequests
            for (ConsentRequestBlock request : providerConsentRequests) {
                if (request.getTransactionId().equals(responseTransactionId)) {
                    // return that request
                    return request;
                }
            }

        }

        return null;
    }
}
