package com.project.EhrRoute.Services;
import com.project.EhrRoute.Core.NodeCluster;
import com.project.EhrRoute.Exceptions.ResourceEmptyException;
import org.springframework.stereotype.Component;

/*
* This class is a singleton spring bean that contains the node clusters that are
* used and shared by multiple controllers.
*/

@Component
public class ClustersContainer
{
    private NodeCluster chainProviders = new NodeCluster();
    private NodeCluster chainConsumers = new NodeCluster();
    private NodeCluster appUsers = new NodeCluster();


    public NodeCluster getConsumersByNetwork(String networkUUID)
    {
        // Cluster to be returned, that is going to contain the network's consumers
        NodeCluster nodeCluster = new NodeCluster();

        if ((chainConsumers.getCluster() == null) && (chainConsumers.getCluster().size() < 1)) {
            throw new ResourceEmptyException("No consumers in chainConsumers cluster");
        }

        // Go through consumers cluster's nodes
        chainConsumers.getCluster().forEach((nodeUUID, Node) -> {
            // Go through each Node networks UUIDs
            Node.getNetworksUUIDs().forEach(nodeNetworkUUID -> {
                // If networkUUID found
                if (nodeNetworkUUID.equals(networkUUID)) {
                    // Add the node with the networkUUID to the cluster
                    nodeCluster.addNode(nodeUUID, Node);
                }
            });
        });

        if (nodeCluster.getCluster().size() < 1) {
            throw new ResourceEmptyException("No consumer in network");
        }

        return nodeCluster;
    }


    public NodeCluster getAppUsers() {
        return appUsers;
    }
    public NodeCluster getChainProviders() {
        return chainProviders;
    }
    public NodeCluster getChainConsumers() {
        return chainConsumers;
    }
    public void setAppUsers(NodeCluster appUsers) {
        this.appUsers = appUsers;
    }
    public void setChainProviders(NodeCluster chainProviders) {
        this.chainProviders = chainProviders;
    }
    public void setChainConsumers(NodeCluster chainConsumers) {
        this.chainConsumers = chainConsumers;
    }
}
