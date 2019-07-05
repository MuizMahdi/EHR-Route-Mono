package com.project.EhrRoute.Core.RTC;
import com.project.EhrRoute.Models.Observer;
import com.project.EhrRoute.Models.Subject;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Optional;


/**
 * A singleton that manages all of the available nodes clusters.
 * A Node is a client in the a network, and node cluster is a group of nodes in a network.
 * A nodes cluster is removed when there is no online node in it.
 */
@Component
public class NodeClustersContainer implements Subject
{
    private HashMap<String, Observer> nodesClusters; // Clusters mapped by their network UUID "<Cluster's network UUID, Cluster>"

    public NodeClustersContainer() {
        this.nodesClusters = new HashMap<>();
    }


    /**
     * Adds a nodes cluster to the container
     * @param nodesCluster  The cluster to be added
     */
    @Override
    public void registerObserver(Observer nodesCluster) {
        nodesClusters.put(nodesCluster.getUUID(), nodesCluster);
    }

    /**
     * Removes a nodes cluster from the container
     * @param nodesCluster   The cluster to be removed
     */
    @Override
    public void removeObserver(Observer nodesCluster) {
        nodesClusters.remove(nodesCluster.getUUID());
    }

    /**
     * Removes a node from all of its registered Node Clusters
     */
    public void removeNode(Node node) {
        nodesClusters.forEach((uuid, cluster) -> ((NodesCluster) cluster).removeObserver(node));
    }

    /**
     * Removes a node from a network's cluster's providers list
     */
    public void removeClusterProviderNode(String nodeUUID, String networkUUID) {
        // Get the network's nodes cluster
        Optional<Observer> networkCluster = findNodesCluster(networkUUID);
        // If the network's cluster exists
        networkCluster.ifPresent(cluster -> {
            // If the node is in the providers list
            ((NodesCluster) cluster).findProvider(nodeUUID).ifPresent(providerNode ->
                // Remove the node from the providers
                ((NodesCluster) cluster).removeProvider(providerNode)
            );
        });
    }

    /**
     * Broadcasts keep-alive heartbeat data to all available nodes of all clusters
     */
    @Override
    public void notifyObservers(Object notification) {
        nodesClusters.forEach((uuid, cluster) -> {
            ((NodesCluster) cluster).notifyObservers(notification);
        });
    }


    /**
     * Finds a nodes cluster by the cluster's network UUID
     * @param networkUUID   The network UUID of the nodes cluster
     * @return              The nodes cluster of the network
     */
    public Optional<Observer> findNodesCluster(String networkUUID) {
        Optional<Observer> nodesCluster;

        try {
            nodesCluster= Optional.of(nodesClusters.get(networkUUID));
        }
        catch (NullPointerException Ex) {
            return Optional.empty();
        }

        return nodesCluster;
    }


    public HashMap<String, Observer> getNodesClusters() {
        return nodesClusters;
    }
    public void setNodesClusters(HashMap<String, Observer> nodesClusters) {
        this.nodesClusters = nodesClusters;
    }
}
