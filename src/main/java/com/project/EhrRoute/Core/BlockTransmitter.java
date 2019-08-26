package com.project.EhrRoute.Core;
import com.project.EhrRoute.Core.RTC.*;
import com.project.EhrRoute.Core.RTC.Node;
import com.project.EhrRoute.Exceptions.ResourceEmptyException;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import com.project.EhrRoute.Models.BlockSource;
import com.project.EhrRoute.Models.Observer;
import com.project.EhrRoute.Payload.Core.SSEs.BlockResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;


@Component
public class BlockTransmitter
{
    private NodeClustersContainer clustersContainer;
    private NodeMessageTransmitter nodeMessageTransmitter;


    @Autowired
    public BlockTransmitter(NodeClustersContainer clustersContainer, NodeMessageTransmitter nodeMessageTransmitter) {
        this.clustersContainer = clustersContainer;
        this.nodeMessageTransmitter = nodeMessageTransmitter;
    }


    public void broadcast(BlockResponse block) throws ResourceEmptyException {

        Optional<Observer> cluster = clustersContainer.findNodesCluster(block.getBlock().getBlockHeader().getNetworkUUID());

        if (!cluster.isPresent()) {
            throw new ResourceNotFoundException("Nodes Cluster", "Network UUID", block.getBlock().getBlockHeader().getNetworkUUID());
        }

        block.getMetadata().setBlockSource(BlockSource.BROADCAST.toString());

        NodesCluster networkNodesCluster = (NodesCluster) cluster.get();

        networkNodesCluster.getConsumingNodes().forEach((nodeUUID, node) -> {
            Long blockId = block.getBlock().getBlockHeader().getIndex();
            nodeMessageTransmitter.sendMessage((Node) node, NodeMessageType.BLOCK, block, blockId.toString());
        });
    }


    public void transmit(BlockResponse blockResponse, String networkUUID, String consumerUUID) {

        Optional<Observer> cluster = clustersContainer.findNodesCluster(networkUUID);

        if (!cluster.isPresent()) {
            throw new ResourceNotFoundException("Nodes Cluster", "Network UUID", networkUUID);
        }

        NodesCluster networkNodesCluster = (NodesCluster) cluster.get();

        // Get the consumer
        Optional<Observer> consumer = networkNodesCluster.findConsumer(consumerUUID);

        if (!consumer.isPresent()) {
            throw new ResourceNotFoundException("Consumer", "Consumer UUID : Network UUID", consumerUUID + " : " + networkUUID);
        }

        // Get the consumer's node
        Node consumerNode = (Node) consumer.get();

        // Transmit the block response to the consumer's node
        nodeMessageTransmitter.sendMessage(consumerNode, NodeMessageType.BLOCK, blockResponse, Long.toString(blockResponse.getBlock().getBlockHeader().getIndex()));
    }
}
