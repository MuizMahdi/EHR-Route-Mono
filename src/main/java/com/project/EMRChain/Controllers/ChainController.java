package com.project.EMRChain.Controllers;
import com.project.EMRChain.Core.Node;
import com.project.EMRChain.Core.NodeCluster;
import com.project.EMRChain.Events.SseKeepAliveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;


@RestController
@RequestMapping("/chain")
public class ChainController
{
    private final Logger logger = LoggerFactory.getLogger(ChainController.class);
    private ExecutorService executorService = Executors.newCachedThreadPool();

    private NodeCluster chainProviders = new NodeCluster();


    @GetMapping("/chainprovider/{nodeuuid}/{netuuid}")
    public SseEmitter subscribeProvider(@PathVariable("nodeuuid") String nodeUUID, @PathVariable("netuuid") String networkUUID)
    {
        // Create an emitter for the subscribed client node
        SseEmitter emitter = new SseEmitter(2592000000L); // An extremely long timeout

        // Create a node which has the emitter and the client's networkUUID
        Node node = new Node(emitter, networkUUID);

        // Add the node to providers list
        this.chainProviders.addNode(nodeUUID, node);

        System.out.println("Node with netUUID: " + this.chainProviders.getNode(nodeUUID).getNetworkUUID() + " Was added to ChainProviders");

        // Remove the emitter on timeout/error/completion

        emitter.onTimeout(() -> {
            this.chainProviders.removeNode(nodeUUID);
            System.out.println("########## EMITTER TIMEOUT ##########");
        });

        emitter.onError(error -> this.chainProviders.removeNode(nodeUUID));

        emitter.onCompletion(() -> {
            this.chainProviders.removeNode(nodeUUID);
            System.out.println("######### EMITTER COMPLETION #########");
        });

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

    @GetMapping("/chainget/{consumeruuid}")
    public ResponseEntity ChainGet(@PathVariable("consumeruuid") String consumerUUID)
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


    @EventListener
    private void SseKeepAlive(SseKeepAliveEvent event)
    {
        chainProviders.getCluster().forEach((uuid, node) -> {
            try
            {
                System.out.println("Sending Keep-Alive Event to node: " + uuid);
                // Send fake data every 4 minutes to keep the connection alive
                event.setKeepAliveData("0"); // Data to be sent
                node.getEmitter().send(event.getKeepAliveData(), MediaType.APPLICATION_JSON);
            }
            catch (IOException Ex) {
                this.chainProviders.removeNode(uuid);
                logger.error(Ex.getMessage());
            }

        });
    }

}
