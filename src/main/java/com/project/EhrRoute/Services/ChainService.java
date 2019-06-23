package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Exceptions.BadRequestException;
import com.project.EhrRoute.Security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ChainService
{
    private UserService userService;
    private NetworkService networkService;
    private BlocksFetchRequestService blocksFetchRequestService;

    @Autowired
    public ChainService(UserService userService, NetworkService networkService, BlocksFetchRequestService blocksFetchRequestService) {
        this.userService = userService;
        this.networkService = networkService;
        this.blocksFetchRequestService = blocksFetchRequestService;
    }


    public void requestBlocksFetch(UserPrincipal recipientUser, String consumerUUID, String networkUUID, int rangeBegin, int rangeEnd) {

        // Get user
        User recipient = userService.findUserById(recipientUser.getId());

        // Get network with network UUID
        Network network = networkService.findNetwork(networkUUID);

        // Validate network membership
        if (!userService.userHasNetwork(recipient, network)) {
            throw new BadRequestException("The recipient node is not a member of the network with UUID: " + networkUUID);
        }

        // Create and save a blocks fetch request
        blocksFetchRequestService.saveBlocksFetchRequest(consumerUUID, networkUUID, rangeBegin, rangeEnd);



    }


}
