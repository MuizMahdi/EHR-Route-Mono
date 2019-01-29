package com.project.EhrRoute.Repositories;
import com.project.EhrRoute.Entities.App.ProviderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProviderDetailsRepository extends JpaRepository<ProviderDetails, Long>
{

}
