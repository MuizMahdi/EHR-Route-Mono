package com.project.EMRChain.Controllers;
import com.project.EMRChain.Core.Node;
import com.project.EMRChain.Core.NodeCluster;
import com.project.EMRChain.Payload.Auth.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
@RequestMapping("/chain")
public class ChainController
{
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private NodeCluster chainProviders = new NodeCluster();


    @GetMapping("/chainprovider/{nodeuuid}/{netuuid}")
    public SseEmitter subscribeProvider(@PathVariable("nodeuuid") String nodeUUID, @PathVariable("netuuid") String networkUUID)
    {
        // Create an emitter for the subscribed client node
        SseEmitter emitter = new SseEmitter();

        // Create a node which has the emitter and the client's networkUUID
        Node node = new Node(emitter, networkUUID);

        // Add the node to providers list
        this.chainProviders.addNode(nodeUUID, node);

        // Remove the emitter on timeout
        emitter.onTimeout(() -> this.chainProviders.removeNode(nodeUUID));

        // Returns the GetChainFromProvider notification SSE
        return emitter;
    }

    @GetMapping("/chainconsumer/{uuid}")
    public SseEmitter chainConsumers(@PathVariable("uuid") String UUID)
    {
        // Todo: Add the client uuid to ChainGetters Cluster

        // Returns notification SSE
        return null;
    }

    @PostMapping("/chaingive")
    public ResponseEntity chainGive()
    {
        // Returns HttpStatus.OK on success
        return null;
    }

    @GetMapping("/chainget")
    public ResponseEntity ChainGet()
    {
        // Returns HttpStatus.OK on success
        return null;
    }

    @GetMapping("/chainupdate")
    public SseEmitter chainUpdate()
    {
        // Returns chain
        return null;
    }


    // Called when client closes app or onDestroy
    @GetMapping("/close/{uuid}")
    public ResponseEntity closeConnection(@PathVariable("uuid") String UUID)
    {
        // Todo: remove the client from clusters
        // Returns chain
        return null;
    }

}
