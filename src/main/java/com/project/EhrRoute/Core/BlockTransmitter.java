package com.project.EhrRoute.Core;
import com.project.EhrRoute.Core.RTC.*;
import com.project.EhrRoute.Core.RTC.Node;
import com.project.EhrRoute.Exceptions.ResourceEmptyException;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import com.project.EhrRoute.Models.Observer;
import com.project.EhrRoute.Payload.Core.SSEs.BlockResponse;
import com.project.EhrRoute.Payload.Core.SerializableBlock;
import com.project.EhrRoute.Services.ClustersContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;


@Component
public class BlockTransmitter
{
    private ClustersContainer clustersContainer;
    private NodeClustersContainer nodeClustersContainer;
    private NodeMessageTransmitter nodeMessageTransmitter;

    @Autowired
    public BlockTransmitter(ClustersContainer clustersContainer, NodeClustersContainer nodeClustersContainer, NodeMessageTransmitter nodeMessageTransmitter) {
        this.clustersContainer = clustersContainer;
        this.nodeClustersContainer = nodeClustersContainer;
        this.nodeMessageTransmitter = nodeMessageTransmitter;
    }


    public void broadcast(SerializableBlock block, String networkUUID) throws ResourceEmptyException {

        // Get NodeCluster of consumers in network, throws ResourceEmptyException if no consumers found
        NodeCluster networkChainConsumers = clustersContainer.getConsumersByNetwork(networkUUID);

        // Send SSEs with block to all consumers in the node's network
        networkChainConsumers.getCluster().forEach((consumerUUID, consumerNode) -> {
            try {
                Long blockID = block.getBlockHeader().getIndex();

                // Send block through a SSE
                SseEmitter.SseEventBuilder event = SseEmitter.event().data(block).id(blockID.toString()).name("block");
                consumerNode.getEmitter().send(event);
            }
            catch (Exception Ex) {
                clustersContainer.getChainConsumers().removeNode(consumerUUID);
                networkChainConsumers.removeNode(consumerUUID);
            }
        });
    }


    public void broadcast(BlockResponse block) throws ResourceEmptyException {

        // Get NodeCluster of consumers in network, throws ResourceEmptyException if no consumers found
        NodeCluster networkChainConsumers = clustersContainer.getConsumersByNetwork(block.getBlock().getBlockHeader().getNetworkUUID());

        // Send SSEs with block to all consumers in the node's network
        networkChainConsumers.getCluster().forEach((consumerUUID, consumerNode) -> {
            try {
                Long blockID = block.getBlock().getBlockHeader().getIndex();

                // Send block through a SSE
                SseEmitter.SseEventBuilder event = SseEmitter.event().data(block).id(blockID.toString()).name("block");
                consumerNode.getEmitter().send(event);
            }
            catch (Exception Ex) {
                clustersContainer.getChainConsumers().removeNode(consumerUUID);
                networkChainConsumers.removeNode(consumerUUID);
            }
        });
    }


    public void transmit(BlockResponse blockResponse, String networkUUID, String consumerUUID) {

        if (!nodeClustersContainer.findNodesCluster(networkUUID).isPresent()) {
            throw new ResourceNotFoundException("Nodes Cluster", "Network UUID", networkUUID);
        }

        NodesCluster networkNodesCluster = (NodesCluster) nodeClustersContainer.findNodesCluster(networkUUID).get();

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
