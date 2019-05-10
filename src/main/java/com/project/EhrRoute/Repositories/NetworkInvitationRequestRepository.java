package com.project.EhrRoute.Repositories;
import com.project.EhrRoute.Entities.App.NetworkInvitationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface NetworkInvitationRequestRepository extends JpaRepository<NetworkInvitationRequest, Long>
{
    Optional<NetworkInvitationRequest> findByInvitationToken(String invitationToken);
    Boolean existsByInvitationToken(String invitationToken);
    Boolean existsByNetworkUUID(String networkUUID);
    Boolean existsBySenderName(String senderName);
}
