package com.project.EMRChain.Repositories;
import com.project.EMRChain.Entities.Core.Network;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NetworkRepository extends JpaRepository<Network, Long>
{
    Optional<Network> findByNetworkUUID(String networkUUID);
}
