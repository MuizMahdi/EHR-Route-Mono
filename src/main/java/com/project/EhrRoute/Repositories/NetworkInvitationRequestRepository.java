package com.project.EhrRoute.Repositories;
import com.project.EhrRoute.Entities.App.NetworkInvitationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface NetworkInvitationRequestRepository extends JpaRepository<NetworkInvitationRequest, Long>
{
    @Query("SELECT r FROM NetworkInvitationRequest r WHERE r.invitationToken = :invitationToken AND r.networkUUID = :networkUUID AND r.senderAddress = :senderAddress")
    Optional<NetworkInvitationRequest> findByInvitationTokenAndNetworkNameAndSenderAddress(@Param("invitationToken") String invitationToken, @Param("networkUUID") String networkUUID, @Param("senderAddress") String senderAddress);
}
