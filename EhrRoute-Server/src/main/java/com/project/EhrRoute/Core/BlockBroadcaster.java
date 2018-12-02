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


    public void broadcast(SerializableBlock block, String nodeUUID) throws ResourceEmptyException
    {
        // Get the node of the node UUID
        Node node = clustersContainer.getChainConsumers().getNode(nodeUUID);

        if (node == null) {
            throw new ResourceNotFoundException("A Node","NodeUUID", nodeUUID);
        }

        // Get node network
        String nodeNetworkUUID = node.getNetworkUUID();

        // Get NodeCluster of consumers in network
        NodeCluster networkChainConsumers = clustersContainer.getConsumersByNetwork(nodeNetworkUUID);

        // Send SSEs with block to all consumers in the node's network
        networkChainConsumers.getCluster().forEach((consumerUUID, consumerNode) -> {
            try {
                consumerNode.getEmitter().send(block);
            }
            catch (Exception Ex) {
                clustersContainer.getChainConsumers().removeNode(consumerUUID);
                networkChainConsumers.removeNode(consumerUUID);
            }
        });

    }
}
