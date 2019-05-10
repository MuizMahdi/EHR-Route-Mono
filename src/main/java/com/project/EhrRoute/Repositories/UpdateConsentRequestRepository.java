package com.project.EhrRoute.Repositories;
import com.project.EhrRoute.Entities.Core.UpdateConsentRequest;
import com.project.EhrRoute.Entities.EHR.EhrDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface UpdateConsentRequestRepository extends JpaRepository<UpdateConsentRequest, Long>
{
    Optional<UpdateConsentRequest> findByEhrDetails(EhrDetails ehrDetails);
}
