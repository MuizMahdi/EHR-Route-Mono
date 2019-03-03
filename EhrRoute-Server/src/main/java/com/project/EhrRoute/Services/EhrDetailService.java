package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.EHR.EhrDetails;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import com.project.EhrRoute.Repositories.EhrDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class EhrDetailService
{
    private EhrDetailsRepository ehrDetailsRepository;

    @Autowired
    public EhrDetailService(EhrDetailsRepository ehrDetailsRepository) {
        this.ehrDetailsRepository = ehrDetailsRepository;
    }


    @Transactional
    public void checkEhrDetailsExistence(EhrDetails ehrDetails) {
        if (!ehrDetailsRepository.existsById(ehrDetails.getId())) {
            throw new ResourceNotFoundException("EHR Details", "ID", ehrDetails.getId());
        }
    }


    @Transactional
    public EhrDetails findEhrDetails(String address) {
        return ehrDetailsRepository.findByAddress(address).orElseThrow(() ->
            new ResourceNotFoundException("EHR Details", "address", address)
        );
    }


    @Transactional
    public EhrDetails findEhrDetailsById(Long id) {
        return ehrDetailsRepository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("EHR Details", "Id", id)
        );
    }


    @Transactional
    public EhrDetails generateUserEhrDetails(String address) {
        // Create EhrDetails using address
        EhrDetails ehrDetails = new EhrDetails(address);
        // Persist it
        return ehrDetailsRepository.save(ehrDetails);
    }


    @Transactional
    public void saveEhrDetails(EhrDetails ehrDetails) {
        ehrDetailsRepository.save(ehrDetails);
    }


    @Transactional
    public void deleteEhrDetails(EhrDetails ehrDetails) {
        ehrDetailsRepository.delete(ehrDetails);
    }
}
