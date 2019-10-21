package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.App.Institution;
import com.project.EhrRoute.Entities.App.ProviderDetails;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import com.project.EhrRoute.Models.RoleName;
import com.project.EhrRoute.Repositories.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class InstitutionService
{
    private InstitutionRepository institutionRepository;
    private ProviderService providerService;
    private UserService userService;

    @Autowired
    public InstitutionService(InstitutionRepository institutionRepository, ProviderService providerService, UserService userService) {
        this.institutionRepository = institutionRepository;
        this.providerService = providerService;
        this.userService = userService;
    }


    @Transactional
    public Institution generateInstitution(String institutionName) {
        return institutionRepository.save(new Institution(institutionName));
    }


    @Transactional
    public Institution getInstitutionByName(String institutionName) {
        return institutionRepository.findByName(institutionName).orElseThrow(() ->
            new ResourceNotFoundException("An institution", "name", institutionName)
        );
    }


    @Transactional
    public void addInstitutionProvider(String userAddress, Long administratorId) {
        // Get the admin's provider details
        ProviderDetails adminProviderDetails = providerService.getProviderDetails(administratorId);

        // Give the user PROVIDER role
        userService.addUserRole(userAddress, RoleName.ROLE_PROVIDER);

        // Create ProviderDetails for the user
        providerService.generateUserProviderDetails(userAddress, adminProviderDetails.getProviderInstitution());
    }

    @Transactional
    public void generateInstitutionProviderDetails(String userAddress, String institutionName) {
        // Generate and save an institution using given name
        Institution institution = generateInstitution(institutionName);
        // Set the generated institution as the provider's institution
        providerService.generateUserProviderDetails(userAddress, institution);
    }
}
