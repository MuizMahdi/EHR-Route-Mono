package com.project.EMRChain.Services;
import com.project.EMRChain.Core.NodeCluster;
import com.project.EMRChain.Exceptions.ResourceEmptyException;
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
        NodeCluster nodeCluster = new NodeCluster();

        if ((chainConsumers.getCluster() == null) && (chainConsumers.getCluster().size() < 1))
        {
            throw new ResourceEmptyException("No consumers in chainConsumers cluster");
        }

        chainConsumers.getCluster().forEach((nodeUUID, Node) -> {
            if (Node.getNetworkUUID().equals(networkUUID)) {
                nodeCluster.addNode(nodeUUID, Node);
            }
        });

        if (nodeCluster.getCluster().size() < 1)
        {
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
