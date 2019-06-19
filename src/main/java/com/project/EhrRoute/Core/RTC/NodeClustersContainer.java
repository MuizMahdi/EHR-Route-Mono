package com.project.EhrRoute.Core.RTC;
import com.project.EhrRoute.Models.Observer;
import com.project.EhrRoute.Models.Subject;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
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
     * Broadcasts keep-alive data to all available nodes of all clusters
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
    public Observer findNodesCluster(String networkUUID) {
        return nodesClusters.get(networkUUID);
    }

    /**
     * Finds if a cluster exists or not by network UUID
     * @return      Boolean that indicates the cluster's existence
     */
    public boolean clusterExists(String networkUUID) {
        return (findNodesCluster(networkUUID) != null);
    }


    public HashMap<String, Observer> getNodesClusters() {
        return nodesClusters;
    }
    public void setNodesClusters(HashMap<String, Observer> nodesClusters) {
        this.nodesClusters = nodesClusters;
    }
}
