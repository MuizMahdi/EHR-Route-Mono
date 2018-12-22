package com.project.EhrRoute.Core;
import com.project.EhrRoute.Exceptions.ResourceEmptyException;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import com.project.EhrRoute.Payload.Core.SerializableBlock;
import com.project.EhrRoute.Services.ClustersContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BlockBroadcaster
{
    private ClustersContainer clustersContainer;

    @Autowired
    public BlockBroadcaster(ClustersContainer clustersContainer) {
        this.clustersContainer = clustersContainer;
    }

    // Broadcasts a block to all consumers in a network
    public void broadcast(SerializableBlock block, String networkUUID) throws ResourceEmptyException
    {
        // Get NodeCluster of consumers in network, throws ResourceEmptyException if no consumers found
        NodeCluster networkChainConsumers = clustersContainer.getConsumersByNetwork(networkUUID);

        // Send SSEs with block to all consumers in the node's network
        networkChainConsumers.getCluster().forEach((consumerUUID, consumerNode) -> {
            try {
                // Send block through a SSE
                consumerNode.getEmitter().send(block);
            }
            catch (Exception Ex) {
                clustersContainer.getChainConsumers().removeNode(consumerUUID);
                networkChainConsumers.removeNode(consumerUUID);
            }
        });
    }
}
