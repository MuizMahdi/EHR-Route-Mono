package com.project.EMRChain.Repositories;
import com.project.EMRChain.Entities.Core.ConsentRequestBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsentRequestBlockRepository extends JpaRepository<ConsentRequestBlock, Long>
{
    Optional<ConsentRequestBlock> findByUserID(Long userID);
}
