package com.project.EhrRoute.Repositories;
import com.project.EhrRoute.Entities.EHR.EhrDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EhrDetailsRepository extends JpaRepository<EhrDetails, Long>
{
    Optional<EhrDetails> findByUuid(String uuid);
}
