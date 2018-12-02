package com.project.EhrRoute.Repositories;
import com.project.EhrRoute.Entities.Core.ChainRoot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface ChainRootRepository extends JpaRepository<ChainRoot, Long>
{
    Optional<ChainRoot> findByRoot(String root);
}
