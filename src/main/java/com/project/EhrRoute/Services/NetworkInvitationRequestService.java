package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.App.NetworkInvitationRequest;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Repositories.NetworkInvitationRequestRepository;
import com.project.EhrRoute.Utilities.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class NetworkInvitationRequestService
{
    private NetworkInvitationRequestRepository networkInvitationRequestRepository;
    private VerificationTokenService verificationTokenService;
    private UuidUtil uuidUtil;

    @Autowired
    public NetworkInvitationRequestService(NetworkInvitationRequestRepository networkInvitationRequestRepository, VerificationTokenService verificationTokenService, UuidUtil uuidUtil) {
        this.networkInvitationRequestRepository = networkInvitationRequestRepository;
        this.verificationTokenService = verificationTokenService;
        this.uuidUtil = uuidUtil;
    }


    @Transactional
    public NetworkInvitationRequest generateInvitationRequest(User recipient, String senderName, String networkName, String networkUUID)
    {
        // Save token on DB, will be used to verify invitation request expiration
        String token = uuidUtil.generateUUID();
        verificationTokenService.createVerificationToken(recipient, token);

        // Create invitation request
        NetworkInvitationRequest invitationRequest = new NetworkInvitationRequest(
            senderName, networkName, networkUUID, token
        );

        // Save invitation request
        saveInvitationRequest(invitationRequest);

        // Return the request object to be added as a Notification reference
        return invitationRequest;
    }


    @Transactional
    public void saveInvitationRequest(NetworkInvitationRequest invitationRequest) {
        networkInvitationRequestRepository.save(invitationRequest);
    }

    @Transactional
    public boolean invitationRequestExists(NetworkInvitationRequest invitationRequest) {
        return networkInvitationRequestRepository.findByInvitationTokenAndNetworkNameAndSenderName(invitationRequest.getInvitationToken(), invitationRequest.getNetworkUUID(), invitationRequest.getSenderName()).isPresent();
    }
}
