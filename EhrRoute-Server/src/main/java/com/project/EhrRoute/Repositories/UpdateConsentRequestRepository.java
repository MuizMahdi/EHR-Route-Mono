package com.project.EhrRoute.Repositories;
import com.project.EhrRoute.Entities.Core.UpdateConsentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UpdateConsentRequestRepository extends JpaRepository<UpdateConsentRequest, Long>
{

}
