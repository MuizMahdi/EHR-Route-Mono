package com.project.EhrRoute.Services;
import com.project.EhrRoute.Core.RTC.*;
import com.project.EhrRoute.Entities.Core.BlocksFetchRequest;
import com.project.EhrRoute.Events.BlocksFetchRequestsCheckEvent;
import com.project.EhrRoute.Exceptions.BadRequestException;
import com.project.EhrRoute.Exceptions.ResourceConflictException;
import com.project.EhrRoute.Models.Observer;
import com.project.EhrRoute.Models.UuidSourceType;
import com.project.EhrRoute.Payload.Core.SSEs.BlockProvideRequest;
import com.project.EhrRoute.Repositories.BlocksFetchRequestRepository;
import com.project.EhrRoute.Utilities.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class BlocksFetchRequestService
{
    private BlocksFetchRequestRepository blocksFetchRequestRepository;
    private NodeMessageTransmitter nodeMessageTransmitter;
    private NodeClustersContainer clustersContainer;
    private UuidUtil uuidUtil;


    @Autowired
    public BlocksFetchRequestService(BlocksFetchRequestRepository blocksFetchRequestRepository, NodeMessageTransmitter nodeMessageTransmitter, NodeClustersContainer clustersContainer, UuidUtil uuidUtil) {
        this.blocksFetchRequestRepository = blocksFetchRequestRepository;
        this.nodeMessageTransmitter = nodeMessageTransmitter;
        this.clustersContainer = clustersContainer;
        this.uuidUtil = uuidUtil;
    }


    @Transactional
    public void saveBlocksFetchRequest(BlocksFetchRequest blocksFetchRequest) {
        blocksFetchRequestRepository.save(blocksFetchRequest);
    }


    public void saveBlocksFetchRequest(String consumerUUID, String networkUUID, long rangeBegin, long rangeEnd) {
        // Validate ranges
        if (rangeBegin < 0 || rangeEnd < 0) throw new BadRequestException("Invalid negative blocks range");

        // Validate UUIDs
        uuidUtil.validateResourceUUID(consumerUUID, UuidSourceType.NODE);
        uuidUtil.validateResourceUUID(networkUUID, UuidSourceType.NETWORK);

        // Check if consumer already has a pending request for the network
        if (blocksFetchRequestExists(consumerUUID, networkUUID)) throw new ResourceConflictException("Another request by the consumer for this network is pending");

        saveBlocksFetchRequest(new BlocksFetchRequest(consumerUUID, networkUUID, rangeBegin, rangeEnd));
    }


    @Transactional
    public Optional<BlocksFetchRequest> findBlocksFetchRequest(String consumerUUID, String networkUUID) {
        return blocksFetchRequestRepository.findByConsumerUUIDAndNetworkUUID(consumerUUID, networkUUID);
    }


    /**
     * Checks if a node has a pending blocks fetch request for a specific network
     * @param consumerUUID      The node UUID
     * @param networkUUID       The network which the node has requested blocks for
     * @return                  A boolean indicating the request existence
     */
    @Transactional
    public boolean blocksFetchRequestExists(String consumerUUID, String networkUUID) {
        return blocksFetchRequestRepository.findByConsumerUUIDAndNetworkUUID(consumerUUID, networkUUID).isPresent();
    }


    @Transactional
    public void deleteBlocksFetchRequest(BlocksFetchRequest blocksFetchRequest) {
       if (blocksFetchRequestExists(blocksFetchRequest.getConsumerUUID(), blocksFetchRequest.getNetworkUUID())) {
           blocksFetchRequestRepository.delete(blocksFetchRequest);
       }
    }


    @EventListener
    protected void sendBlocksFetchRequest(BlocksFetchRequestsCheckEvent event) {
        // Find all currently open/issued blocks fetch requests
        List<BlocksFetchRequest> blocksFetchRequests = blocksFetchRequestRepository.findAll();

        if (!blocksFetchRequests.isEmpty()) {
            for (BlocksFetchRequest fetchRequest : blocksFetchRequests) {
                // Get the cluster of the network in the issued fetch request
                Optional<Observer> cluster = clustersContainer.findNodesCluster(fetchRequest.getNetworkUUID());

                // Skip if cluster doesn't exist (no available nodes)
                if (!cluster.isPresent()) continue;

                // Get a random provider from the cluster
                Optional<Observer> provider = ((NodesCluster) cluster.get()).getRandomProvider();

                provider.ifPresent(providerNode -> {
                    // Construct a block provider request payload object
                    BlockProvideRequest request = new BlockProvideRequest(fetchRequest.getConsumerUUID(), fetchRequest.getNetworkUUID(), fetchRequest.getBlocksRangeBegin());
                    // Send the request
                    nodeMessageTransmitter.sendMessage((Node) providerNode, NodeMessageType.BLOCK_PROVIDE_REQUEST, request);
                });
            }
        }
    }
}
