package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.App.ProviderDetails;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Repositories.ProviderDetailsRepository;
import com.project.EhrRoute.Utilities.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProviderService
{
    private UuidUtil uuidUtil;
    private ProviderDetailsRepository providerDetailsRepository;

    @Autowired
    public ProviderService(UuidUtil uuidUtil, ProviderDetailsRepository providerDetailsRepository) {
        this.uuidUtil = uuidUtil;
        this.providerDetailsRepository = providerDetailsRepository;
    }


    @Transactional
    public void generateProviderDetails(User user)
    {
        // Generate UUID
        String providerUUID = uuidUtil.generateUUID();

        // Create provider details for user
        ProviderDetails providerDetails = new ProviderDetails(user, providerUUID);

        // Persist the provider details
        providerDetailsRepository.save(providerDetails);
    }
}
