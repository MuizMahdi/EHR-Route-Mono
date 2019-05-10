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
    private UuidUtil uuidUtil;
    private ProviderDetailsRepository providerDetailsRepository;
    private InstitutionService institutionService;

    @Autowired
    public ProviderService(UuidUtil uuidUtil, ProviderDetailsRepository providerDetailsRepository, InstitutionService institutionService) {
        this.uuidUtil = uuidUtil;
        this.providerDetailsRepository = providerDetailsRepository;
        this.institutionService = institutionService;
    }


    @Transactional
    public void generateUserProviderDetails(User user, String institutionName)
    {
        // Find an institution with the given institution name
        Institution institution = institutionService.getInstitutionByName(institutionName);

        // Set the found institution of the user that sent the role change token as this users's institution
        setProviderInstitution(user, institution);
    }


    @Transactional
    public void generateInstitutionProviderDetails(User user, String institutionName)
    {
        // Generate and save an institution using given name
        Institution institution = institutionService.generateInstitution(institutionName);

        // Set the generated institution as the provider's institution
        setProviderInstitution(user, institution);
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


    @Transactional
    public String getProviderInstitution(Long userID) throws ResourceNotFoundException
    {
        return providerDetailsRepository.findProviderInstitutionByUserID(userID).orElseThrow(() ->
            new ResourceNotFoundException("Institution of a provider", "user ID", userID)
        );
    }


    @Transactional
    private void setProviderInstitution(User user, Institution institution) throws ResourceNotFoundException
    {
        // Generate UUID
        String providerUUID = uuidUtil.generateUUID();

        // Create provider details for user
        ProviderDetails providerDetails = new ProviderDetails(user, providerUUID);

        // Address is sent by user at login
        providerDetails.setProviderAddress("");

        // Set the institution as the provider's institution
        providerDetails.setProviderInstitution(institution);

        // Persist the provider details
        providerDetailsRepository.save(providerDetails);
    }
}
