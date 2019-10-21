package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.App.Institution;
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
    private ProviderDetailsRepository providerDetailsRepository;
    private UserService userService;
    private UuidUtil uuidUtil;

    @Autowired
    public ProviderService(ProviderDetailsRepository providerDetailsRepository, UserService userService, UuidUtil uuidUtil) {
        this.providerDetailsRepository = providerDetailsRepository;
        this.userService = userService;
        this.uuidUtil = uuidUtil;
    }


    @Transactional
    public ProviderDetails getProviderDetails(Long providerUserId) {
        return providerDetailsRepository.findProviderDetailsByUserID(providerUserId).orElseThrow(() ->
            new ResourceNotFoundException("Provider Details", "Provider User ID", providerUserId)
        );
    }

    @Transactional
    public void generateUserProviderDetails(String userAddress, Institution institution) {
        // Find user using address
        User user = userService.findUserByAddress(userAddress);
        // Generate provider UUID
        String providerUUID = uuidUtil.generateUUID();
        // Create and save a provider details
        saveProviderDetails(new ProviderDetails(user, userAddress, institution, providerUUID));
    }


    @Transactional
    public void setProviderAddress(User user, String address) throws ResourceNotFoundException {
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
    public String getProviderUuidByUserID(Long userID) throws ResourceNotFoundException {
        return providerDetailsRepository.findProviderUUIDByUserID(userID).orElseThrow(() ->
            new ResourceNotFoundException("Provider", "User Id", userID)
        );
    }


    @Transactional
    public boolean providerAddressExists(Long userID) {
        String address = providerDetailsRepository.findProviderAddressByUserID(userID).orElse(null);
        return !(address == null || address.isEmpty());
    }


    @Transactional
    public String getProviderAddress(Long userID) throws ResourceNotFoundException {
        return providerDetailsRepository.findProviderAddressByUserID(userID).orElseThrow(() ->
            new ResourceNotFoundException("Address for a provider", "user ID", userID)
        );
    }


    @Transactional
    public String getProviderInstitution(Long userID) throws ResourceNotFoundException {
        return providerDetailsRepository.findProviderInstitutionByUserID(userID).orElseThrow(() ->
            new ResourceNotFoundException("Institution of a provider", "user ID", userID)
        );
    }


    @Transactional
    public ProviderDetails saveProviderDetails(ProviderDetails providerDetails) {
        providerDetailsRepository.save(providerDetails);
        return providerDetails;
    }
}
