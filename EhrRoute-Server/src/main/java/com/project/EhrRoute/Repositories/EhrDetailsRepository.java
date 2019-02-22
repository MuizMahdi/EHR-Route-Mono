package com.project.EhrRoute.Repositories;
import com.project.EhrRoute.Entities.EHR.EhrDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EhrDetailsRepository extends JpaRepository<EhrDetails, Long>
{

}
