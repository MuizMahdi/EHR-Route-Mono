package com.project.EhrRoute.Repositories;
import com.project.EhrRoute.Entities.App.ProviderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProviderDetailsRepository extends JpaRepository<ProviderDetails, Long>
{
    @Query("SELECT p FROM ProviderDetails p INNER JOIN p.user u WHERE u.id = :userID")
    Optional<ProviderDetails> findProviderDetailsByUserID(@Param("userID") Long id);

    @Query("SELECT p.providerUUID FROM ProviderDetails p INNER JOIN p.user u WHERE u.id = :userID")
    Optional<String> findProviderUUIDByUserID(@Param("userID") Long id);
}
