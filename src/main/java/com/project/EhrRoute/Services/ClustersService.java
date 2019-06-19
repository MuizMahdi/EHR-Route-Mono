package com.project.EhrRoute.Services;
import com.project.EhrRoute.Core.RTC.Node;
import com.project.EhrRoute.Core.RTC.NodeClustersContainer;
import com.project.EhrRoute.Core.RTC.NodesCluster;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Events.SseKeepAliveEvent;
import com.project.EhrRoute.Models.NodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class ClustersService
{
    private NodeClustersContainer clustersContainer;

    @Autowired
    public ClustersService(NodeClustersContainer clustersContainer) {
        this.clustersContainer = clustersContainer;
    }

    /**
     * Adds a user's node to the user's networks' clusters'
     * @param user          The user
     * @param node          The user's node
     * @param nodeType      Specifies whether the node is going to be added to consumers or providers
     */
    public void subscribeUserNode(User user, Node node, NodeType nodeType) {

        Set<Network> userNetworks = user.getNetworks();

        if (userNetworks.size() > 0) {
            userNetworks.forEach(network -> {
                // If a node cluster exists for the network
                if (clustersContainer.clusterExists(network.getNetworkUUID())) {
                    // Get the cluster
                    NodesCluster networkCluster = (NodesCluster) clustersContainer.findNodesCluster(network.getNetworkUUID());
                    // Add the user's node to the cluster
                    registerClusterNode(node, nodeType, networkCluster);
                }
                else {
                    // Create a cluster
                    NodesCluster networkCluster = new NodesCluster(network.getNetworkUUID());
                    // Add the user's node to the cluster
                    registerClusterNode(node, nodeType, networkCluster);
                    // Add the cluster to the container
                    clustersContainer.registerObserver(networkCluster);
                }
            });
        }
    }

    /**
     * Adds a node to a nodes cluster list of specific type
     * @param node          The node to be added
     * @param nodeType      The node type for the list
     * @param cluster       The nodes cluster to add the node into
     */
    private void registerClusterNode(Node node, NodeType nodeType, NodesCluster cluster) {
        switch (nodeType) {
            case PROVIDER: cluster.registerProvider(node);
            case CONSUMER: cluster.registerConsumer(node);
        }
    }

    /**
     * Finds a user's networks' clusters list
     * @param user      The user
     * @return          A list of user's networks clusters
     */
    public List<NodesCluster> getUserNetworksClusters(User user) {
        Set<Network> userNetworks = user.getNetworks();
        List<NodesCluster> clusters = new ArrayList<>();

        if (userNetworks.size() > 0) {
            userNetworks.forEach(network -> {
                if (clustersContainer.clusterExists(network.getNetworkUUID())) {
                    clusters.add((NodesCluster) clustersContainer.findNodesCluster(network.getNetworkUUID()));
                }
            });
        }

        return clusters;
    }

    /**
     * Removes a user's nodes from all of the user's networks nodes clusters
     * @param user          The user
     * @param nodeUUID      The user's node UUID
     */
    public void removeUserNodes(User user, String nodeUUID) {
        List<NodesCluster> userNodesClusters = getUserNetworksClusters(user);

        userNodesClusters.forEach(cluster -> {
            if (cluster.findConsumer(nodeUUID).isPresent()) {
                Node consumer = (Node) cluster.findConsumer(nodeUUID).get();
                consumer.getEmitter().complete();
                cluster.removeConsumer(consumer);
            }
            if (cluster.findProvider(nodeUUID).isPresent()) {
                Node provider = (Node) cluster.findProvider(nodeUUID).get();
                provider.getEmitter().complete();
                cluster.removeProvider(provider);
            }
        });
    }

    /**
     * Sends an event every 30 seconds to keep the nodes connection alive and to filter out disconnected nodes
     * @param event     The event data to be sent
     */
    @EventListener
    private void broadcastKeepAliveEvent(SseKeepAliveEvent event) {
        clustersContainer.notifyObservers(event.getKeepAliveData());
    }
}
