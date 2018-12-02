package com.project.EhrRoute.Repositories;
import com.project.EhrRoute.Entities.Core.Network;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NetworkRepository extends JpaRepository<Network, Long>
{
    Optional<Network> findByNetworkUUID(String networkUUID);
    //Optional<ChainRoot> findChainRootByNetworkUUID
}
