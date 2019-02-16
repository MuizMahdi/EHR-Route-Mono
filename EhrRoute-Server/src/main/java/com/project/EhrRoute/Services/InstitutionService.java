package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.App.Institution;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import com.project.EhrRoute.Repositories.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class InstitutionService
{
    private InstitutionRepository institutionRepository;

    @Autowired
    public InstitutionService(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }


    @Transactional
    public Institution getInstitutionByName(String institutionName) {
        return institutionRepository.findByName(institutionName).orElseThrow(() ->
            new ResourceNotFoundException("An institution", "name", institutionName)
        );
    }
}
