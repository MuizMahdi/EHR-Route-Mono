package com.project.EMRChain.Services;
import com.project.EMRChain.Core.NodeCluster;
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
