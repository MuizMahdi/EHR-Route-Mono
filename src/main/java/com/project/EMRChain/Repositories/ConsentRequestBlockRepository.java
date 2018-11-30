package com.project.EMRChain.Repositories;
import com.project.EMRChain.Entities.Core.ConsentRequestBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsentRequestBlockRepository extends JpaRepository<ConsentRequestBlock, Long>
{
    Optional<ConsentRequestBlock> findByUserID(Long userID);

    @Query("SELECT Rq FROM ConsentRequestBlock Rq WHERE Rq.providerUUID = :providerUuid")
    List<ConsentRequestBlock> findByProviderUUID(@Param("providerUuid") String providerUUID);
}
