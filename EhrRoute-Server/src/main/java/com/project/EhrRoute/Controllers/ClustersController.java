package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Core.Node;
import com.project.EhrRoute.Core.NodeCluster;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Events.SseKeepAliveEvent;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Security.CurrentUser;
import com.project.EhrRoute.Security.UserPrincipal;
import com.project.EhrRoute.Services.ClustersContainer;
import com.project.EhrRoute.Services.UserService;
import com.project.EhrRoute.Utilities.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/cluster")
public class ClustersController
{
    private final Logger logger = LoggerFactory.getLogger(ClustersController.class);
    private ClustersContainer clustersContainer;
    private UserService userService;
    private UuidUtil uuidUtil;

    @Autowired
    public ClustersController(ClustersContainer clustersContainer, UserService userService, UuidUtil uuidUtil) {
        this.clustersContainer = clustersContainer;
        this.userService = userService;
        this.uuidUtil = uuidUtil;
    }

    // Creates a node for current user and adds it to the chain providers cluster (used to receive a ChainSend SSE)
    @GetMapping("/chainprovider")
    @PreAuthorize("hasRole('ADMIN')")
    public SseEmitter subscribeProvider(@RequestParam("nodeuuid") String nodeUUID, @CurrentUser UserPrincipal currentUser) throws IOException
    {
        // Create an emitter for the subscribed client node
        SseEmitter emitter = new SseEmitter(2592000000L); // An extremely long timeout

        if (!uuidUtil.isValidUUID(nodeUUID)) {
            emitter.send("Invalid node UUID", MediaType.APPLICATION_JSON);
        }
        else {
            // Get user
            User user = userService.findUserByUsernameOrEmail(currentUser.getUsername());

            Node node = generateUserNode(emitter, user);

            // Add the node to the providers cluster
            clustersContainer.getChainProviders().addNode(nodeUUID, node);
        }


        // Remove the emitter on timeout/error/completion
        emitter.onTimeout(() -> clustersContainer.getChainProviders().removeNode(nodeUUID));
        emitter.onError(error -> clustersContainer.getChainProviders().removeNode(nodeUUID));
        emitter.onCompletion(() -> clustersContainer.getChainProviders().removeNode(nodeUUID));

        // Returns the GetChainFromProviderEvent notification SSE
        return emitter;
    }


    // Creates a node for current user and adds it to the chain consumers cluster (used to receive the chain from a provider)
    @GetMapping("/chainconsumer")
    @PreAuthorize("hasRole('ADMIN')")
    public SseEmitter chainConsumers(@RequestParam("nodeuuid") String nodeUUID, @CurrentUser UserPrincipal currentUser) throws IOException
    {
        SseEmitter emitter = new SseEmitter(2592000000L);

        if (!uuidUtil.isValidUUID(nodeUUID)) {
            emitter.send("Invalid node UUID", MediaType.APPLICATION_JSON);
        }
        else {
            // Get user
            User user = userService.findUserByUsernameOrEmail(currentUser.getUsername());

            Node node = generateUserNode(emitter, user);

            clustersContainer.getChainConsumers().addNode(nodeUUID, node);
        }

        // Remove the emitter on timeout/error/completion
        emitter.onTimeout(() -> clustersContainer.getChainConsumers().removeNode(nodeUUID));
        emitter.onError(error -> clustersContainer.getChainConsumers().removeNode(nodeUUID));
        emitter.onCompletion(() -> clustersContainer.getChainConsumers().removeNode(nodeUUID));

        // Returns the ChainSend and BlockSend SSE
        return emitter;
    }


    // Called when client closes app (ngOnDestroy) to remove node from clusters
    @GetMapping("/close")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity closeConnection(@RequestParam("nodeuuid") String nodeUUID)
    {
        // Remove the client from clusters
        if(clustersContainer.getChainConsumers().existsInCluster(nodeUUID)) {
            clustersContainer.getChainConsumers().removeNode(nodeUUID);
        }

        if (clustersContainer.getChainProviders().existsInCluster(nodeUUID)) {
            clustersContainer.getChainProviders().removeNode(nodeUUID);
        }

        return new ResponseEntity<>(
            new ApiResponse(true, "Connection closed"),
            HttpStatus.OK
        );
    }


    // Generates a Node that has the emitter and the user networks
    private Node generateUserNode(SseEmitter emitter, User user)
    {
        // Get user networks
        Set<Network> userNetworks = user.getNetworks();
        Set<String> networkUUIDs = new HashSet<>();

        // Get networks UUIDs of user networks
        userNetworks.forEach(network -> {
            // Add network UUID to set
            networkUUIDs.add(network.getNetworkUUID());
        });

        // Create a node with the networkUUIDs set
        Node node = new Node(emitter, networkUUIDs);

        return node;
    }


    @EventListener
    protected void SseKeepAlive(SseKeepAliveEvent event)
    {
        // Keep-Alive fake data
        String keepAliveData = event.getKeepAliveData();

        NodeCluster chainProviders = clustersContainer.getChainProviders();
        broadcastKeepAliveEvent(chainProviders, keepAliveData);

        NodeCluster chainConsumers = clustersContainer.getChainConsumers();
        broadcastKeepAliveEvent(chainConsumers, keepAliveData);
    }


    private void broadcastKeepAliveEvent(NodeCluster cluster, String keepAliveData)
    {
        if ((cluster.getCluster() != null) && (!cluster.getCluster().isEmpty()))
        {
            // Cluster iterator
            Iterator clusterIterator = cluster.getCluster().entrySet().iterator();

            // Iterate through the cluster
            while (clusterIterator.hasNext())
            {
                // Get each member of cluster (HashMap entry of the cluster)
                Map.Entry clusterEntry = (Map.Entry) clusterIterator.next();

                // Get each member node and node UUID
                Node node = (Node) clusterEntry.getValue();
                String nodeUUID = (String) clusterEntry.getKey();

                try {
                    // Send fake data every minute to keep the connection alive and check whether the user disconnected or not
                    node.getEmitter().send(keepAliveData, MediaType.APPLICATION_JSON);
                }
                // In case an error occurs during event transmission
                catch (Exception Ex) {
                    // Remove provider map using iterator entry to avoid ConcurrentModificationException
                    clusterIterator.remove();

                    // Remove the provider from cluster
                    cluster.removeNode(nodeUUID);
                }
            }
        }
    }

}
