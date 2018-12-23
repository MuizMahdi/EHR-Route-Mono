package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Core.Node;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Security.CurrentUser;
import com.project.EhrRoute.Security.UserPrincipal;
import com.project.EhrRoute.Services.ClustersContainer;
import com.project.EhrRoute.Services.UserService;
import com.project.EhrRoute.Utilities.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Set;


@RestController
@RequestMapping("/cluster")
public class ClustersController
{
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
    public ResponseEntity closeConnection(@RequestParam("uuid") String uuid)
    {
        // Remove the client from clusters
        if(clustersContainer.getChainConsumers().existsInCluster(uuid)) {
            clustersContainer.getChainConsumers().removeNode(uuid);
        }

        if (clustersContainer.getChainProviders().existsInCluster(uuid)) {
            clustersContainer.getChainProviders().removeNode(uuid);
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
}
