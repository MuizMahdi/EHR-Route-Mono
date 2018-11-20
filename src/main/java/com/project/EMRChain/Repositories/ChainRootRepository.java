package com.project.EMRChain.Repositories;
import com.project.EMRChain.Entities.App.ChainRoot;
import com.project.EMRChain.Entities.App.Network;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface ChainRootRepository extends JpaRepository<ChainRoot, Long>
{
    Optional<ChainRoot> findByNetwork(Network network);
}
