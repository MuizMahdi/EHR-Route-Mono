package com.project.EhrRoute.Repositories;
import com.project.EhrRoute.Entities.Core.Network;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface NetworkRepository extends JpaRepository<Network, Long>
{
    Optional<Network> findByNetworkUUID(String networkUUID);

    @Query("SELECT n.name FROM Network n WHERE n.name LIKE CONCAT('%', :keyword, '%')")
    List<String> searchNetworksByName(@Param("keyword") String keyword);

    @Query("SELECT n.networkUUID FROM Network n WHERE n.name = :networkName")
    Optional<String> getNetworkUUIDByName(@Param("networkName") String name);
}
