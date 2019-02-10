package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.App.ProviderDetails;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
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

        // Address is sent by user at login
        providerDetails.setProviderAddress("");

        // Persist the provider details
        providerDetailsRepository.save(providerDetails);
    }


    @Transactional
    public void setProviderAddress(User user, String address) throws ResourceNotFoundException
    {
        // Get user's provider details
        ProviderDetails providerDetails = providerDetailsRepository.findProviderDetailsByUserID(
            user.getId()
        ).orElseThrow(() ->
            new ResourceNotFoundException("Provider Details", "User ID", user.getId())
        );

        // Add the address
        providerDetails.setProviderAddress(address);

        // Persist changes
        providerDetailsRepository.save(providerDetails);
    }


    @Transactional
    public String getProviderUuidByUserID(Long userID) throws ResourceNotFoundException
    {
        return providerDetailsRepository.findProviderUUIDByUserID(userID).orElseThrow(() ->
            new ResourceNotFoundException("User", "ID", userID)
        );
    }


    @Transactional
    public boolean providerAddressExists(Long userID)
    {
        String address = providerDetailsRepository.findProviderAddressByUserID(userID).orElse(null);
        return !(address == null || address.isEmpty());
    }


    @Transactional
    public String getProviderAddress(Long userID) throws ResourceNotFoundException
    {
        return providerDetailsRepository.findProviderAddressByUserID(userID).orElseThrow(() ->
            new ResourceNotFoundException("Address for a provider", "user ID", userID)
        );
    }
}
