package com.project.EhrRoute.Repositories;
import com.project.EhrRoute.Entities.Core.BlocksFetchRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface BlocksFetchRequestRepository extends JpaRepository<BlocksFetchRequest, Long>
{
    @Query("SELECT r FROM BlocksFetchRequest r WHERE r.consumerUUID = :consumerUUID AND r.networkUUID = :networkUUID")
    Optional<BlocksFetchRequest> exists(@Param("consumerUUID") String consumerUuid, @Param("networkUUID") String networkUuid);
}
