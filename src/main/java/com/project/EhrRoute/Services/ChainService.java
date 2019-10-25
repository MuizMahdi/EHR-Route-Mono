package com.project.EhrRoute.Services;
import com.project.EhrRoute.Core.BlockTransmitter;
import com.project.EhrRoute.Core.RTC.NodeClustersContainer;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Entities.Core.BlocksFetchRequest;
import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Exceptions.BadRequestException;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import com.project.EhrRoute.Models.BlockSource;
import com.project.EhrRoute.Payload.Core.BlockFetchResponse;
import com.project.EhrRoute.Security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class ChainService
{
    private UserService userService;
    private NetworkService networkService;
    private BlockTransmitter blockTransmitter;
    private NodeClustersContainer clustersContainer;
    private BlocksFetchRequestService blocksFetchRequestService;

    @Autowired
    public ChainService(UserService userService, NetworkService networkService, BlockTransmitter blockTransmitter, NodeClustersContainer clustersContainer, BlocksFetchRequestService blocksFetchRequestService) {
        this.userService = userService;
        this.networkService = networkService;
        this.blockTransmitter = blockTransmitter;
        this.clustersContainer = clustersContainer;
        this.blocksFetchRequestService = blocksFetchRequestService;
    }

    /**
     *
     * @param recipientUser
     * @param consumerUUID
     * @param networkUUID
     * @param rangeBegin
     * @param rangeEnd
     */
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

        // Remove the node from the providers list of the network of request blocks (so the same node wouldn't be picked as a provider for their own request)
        clustersContainer.removeClusterProviderNode(consumerUUID, networkUUID);
    }

    /**
     *
     * @param blockFetchResponse
     */
    public void sendFetchedBlock(BlockFetchResponse blockFetchResponse) {

        String consumerUUID = blockFetchResponse.getConsumerUUID();
        String networkUUID = blockFetchResponse.getNetworkUUID();

        Optional<BlocksFetchRequest> blocksFetchRequest = blocksFetchRequestService.findBlocksFetchRequest(consumerUUID, networkUUID);

        // Verify that a fetch request exists with sent data
        if (!blocksFetchRequest.isPresent()) {
            throw new ResourceNotFoundException("Blocks fetch request", "Consumer UUID : Network UUID", consumerUUID + " : " + networkUUID);
        }

        String realChainRoot = networkService.getNetworkChainRoot(networkUUID);

        // Verify network's chain root
        if (!realChainRoot.equals(blockFetchResponse.getNetworkChainRoot())) {
            throw new BadRequestException("Invalid chain root for the network with UUID of " + networkUUID);
        }

        // Set the block source type
        blockFetchResponse.getBlockResponse().getMetadata().setBlockSource(BlockSource.FETCH_REQUEST.toString());

        // Send the block to the consumer with consumerUUID
        blockTransmitter.transmit(blockFetchResponse.getBlockResponse(), networkUUID, consumerUUID);

        BlocksFetchRequest blocksRequest = blocksFetchRequest.get();

        // Increment the blocks range begin of the blocks fetch request
        blocksRequest.setBlocksRangeBegin(blocksRequest.getBlocksRangeBegin() + 1);
        // Set the blocks range end as the provider has specified, this allows it to change with every blocks fetch response,
        // which allows consumers to receive even the blocks that have been added before the blocks fetch request has ended.
        blocksRequest.setBlocksRangeEnd(blockFetchResponse.getNetworkLatestBlockIndex());
        blocksFetchRequestService.saveBlocksFetchRequest(blocksRequest);

        // If no more requested blocks are left (range begin > range end)
        if (blocksRequest.getBlocksRangeBegin() > blocksRequest.getBlocksRangeEnd()) {
            // Delete blocks fetch request
            blocksFetchRequestService.deleteBlocksFetchRequest(blocksRequest);
            // Resubscribe the node to the providers list of the network
            clustersContainer.registerClusterProviderNode(blockFetchResponse.getConsumerUUID(), blockFetchResponse.getNetworkUUID());
        }
    }
}
