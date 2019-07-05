package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Core.RTC.Node;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Models.NodeType;
import com.project.EhrRoute.Models.UuidSourceType;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Security.CurrentUser;
import com.project.EhrRoute.Security.UserPrincipal;
import com.project.EhrRoute.Services.ClustersContainer;
import com.project.EhrRoute.Services.ClustersService;
import com.project.EhrRoute.Services.UserService;
import com.project.EhrRoute.Utilities.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequestMapping("/cluster")
@PreAuthorize("hasRole('PROVIDER')")
public class ClustersController
{
    private final Logger logger = LoggerFactory.getLogger(ClustersController.class);
    private ClustersContainer clustersContainer;
    private ClustersService clustersService;
    private UserService userService;
    private UuidUtil uuidUtil;

    @Autowired
    public ClustersController(ClustersContainer clustersContainer, ClustersService clustersService, UserService userService, UuidUtil uuidUtil) {
        this.clustersContainer = clustersContainer;
        this.clustersService = clustersService;
        this.userService = userService;
        this.uuidUtil = uuidUtil;
    }


    @GetMapping("/providers")
    public SseEmitter subscribeProviderNode(@RequestParam("nodeuuid") String nodeUUID, @CurrentUser UserPrincipal currentUser)
    {
        // Validate node UUID
        uuidUtil.validateResourceUUID(nodeUUID, UuidSourceType.NODE);

        // Create an emitter for the node
        SseEmitter emitter = new SseEmitter(2592000000L);

        // Get current user
        User user = userService.findUserById(currentUser.getId());

        // Create a node for the user
        Node userNode = new Node(nodeUUID, emitter);

        // Add the user's node to the user's networks' clusters' providers list
        clustersService.subscribeUserNode(user, userNode, NodeType.PROVIDER);

        logger.info("Node [" + nodeUUID + "] has been added to its networks' clusters' providers list.");

        // Remove the emitter on timeout/error/completion
        emitter.onTimeout(() -> clustersContainer.getChainProviders().removeNode(nodeUUID));
        emitter.onError(error -> clustersContainer.getChainProviders().removeNode(nodeUUID));
        emitter.onCompletion(() -> clustersContainer.getChainProviders().removeNode(nodeUUID));

        return emitter;
    }


    @GetMapping("/consumers")
    public SseEmitter subscribeConsumerNode(@RequestParam("nodeuuid") String nodeUUID, @CurrentUser UserPrincipal currentUser)
    {
        // Validate node UUID
        uuidUtil.validateResourceUUID(nodeUUID, UuidSourceType.NODE);

        // Create an emitter for the node
        SseEmitter emitter = new SseEmitter(2592000000L);

        // Get current user
        User user = userService.findUserById(currentUser.getId());

        // Create a node for the user
        Node userNode = new Node(nodeUUID, emitter);

        // Add the user's node to the user's networks' clusters' providers list
        clustersService.subscribeUserNode(user, userNode, NodeType.CONSUMER);

        logger.info("Node [" + nodeUUID + "] has been added to its networks' clusters' consumers list.");

        // Remove the emitter on timeout/error/completion
        emitter.onTimeout(() -> clustersContainer.getChainProviders().removeNode(nodeUUID));
        emitter.onError(error -> clustersContainer.getChainProviders().removeNode(nodeUUID));
        emitter.onCompletion(() -> clustersContainer.getChainProviders().removeNode(nodeUUID));

        return emitter;
    }


    @GetMapping("/close")
    public ResponseEntity closeConnection(@RequestParam("nodeuuid") String nodeUUID, @CurrentUser UserPrincipal currentUser)
    {
        // Validate node UUID
        uuidUtil.validateResourceUUID(nodeUUID, UuidSourceType.NODE);

        // Get current user
        User user = userService.findUserById(currentUser.getId());

        // Remove the user's nodes from all clusters
        clustersService.removeUserNodes(user, nodeUUID);

        return ResponseEntity.ok(new ApiResponse(true, "Connection closed"));
    }
}
