package com.project.EhrRoute.Repositories;
import com.project.EhrRoute.Entities.Core.BlocksFetchRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BlocksFetchRequestRepository extends JpaRepository<BlocksFetchRequest, Long>
{

}
