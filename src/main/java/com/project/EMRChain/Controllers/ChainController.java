package com.project.EMRChain.Controllers;
import com.project.EMRChain.Core.Node;
import com.project.EMRChain.Core.NodeCluster;
import com.project.EMRChain.Events.GetChainFromProviderEvent;
import com.project.EMRChain.Events.SendChainToConsumerEvent;
import com.project.EMRChain.Events.SseKeepAliveEvent;
import com.project.EMRChain.Payload.Auth.ApiResponse;
import com.project.EMRChain.Payload.Core.SerializableChain;
import com.project.EMRChain.Services.ClustersContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/chain")
public class ChainController
{
    private ApplicationEventPublisher eventPublisher;
    private ClustersContainer clustersContainer;

    @Autowired
    public ChainController(ClustersContainer clustersContainer, ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        this.clustersContainer = clustersContainer;
    }

    private final Logger logger = LoggerFactory.getLogger(ChainController.class);
    //private ExecutorService executorService = Executors.newCachedThreadPool();
    //private NodeCluster chainProviders = new NodeCluster();
    //private NodeCluster chainConsumers = new NodeCluster();


    // Subscribes a node to the chain providers cluster (used to receive a ChainSend SSE)
    @GetMapping("/chainprovider")
    @PreAuthorize("hasRole('ADMIN')")
    public SseEmitter subscribeProvider(@RequestParam("nodeuuid") String nodeUUID, @RequestParam("netuuid") String networkUUID) throws IOException
    {
        // Create an emitter for the subscribed client node
        SseEmitter emitter = new SseEmitter(2592000000L); // An extremely long timeout

        if (!isValidUUID(nodeUUID) || !isValidUUID(networkUUID))
        {
            emitter.send("Invalid node or network UUID", MediaType.APPLICATION_JSON);
        }
        else
        {
            // Create a node which has the emitter and the client's networkUUID
            Node node = new Node(emitter, networkUUID);

            // Add the node to providers list
            clustersContainer.getChainProviders().addNode(nodeUUID, node);

            System.out.println("Node with netUUID: " + clustersContainer.getChainProviders().getNode(nodeUUID).getNetworkUUID() + " Was added to ChainProviders");
        }


        // Remove the emitter on timeout/error/completion
        emitter.onTimeout(() -> clustersContainer.getChainProviders().removeNode(nodeUUID));
        emitter.onError(error -> clustersContainer.getChainProviders().removeNode(nodeUUID));
        emitter.onCompletion(() -> clustersContainer.getChainProviders().removeNode(nodeUUID));

        // Returns the GetChainFromProviderEvent notification SSE
        return emitter;
    }

    // Subscribes a node to the chain consumers cluster (used to receive the chain from a provider)
    @GetMapping("/chainconsumer")
    @PreAuthorize("hasRole('ADMIN')")
    public SseEmitter chainConsumers(@RequestParam("nodeuuid") String nodeUUID, @RequestParam("netuuid") String networkUUID) throws IOException
    {
        SseEmitter emitter = new SseEmitter(2592000000L);

        if (!isValidUUID(nodeUUID) || !isValidUUID(networkUUID)) {
            emitter.send("Invalid node or network UUID", MediaType.APPLICATION_JSON);
        }
        else {
            Node node = new Node(emitter, networkUUID);
            clustersContainer.getChainConsumers().addNode(nodeUUID, node);
        }

        // Remove the emitter on timeout/error/completion
        emitter.onTimeout(() -> clustersContainer.getChainConsumers().removeNode(nodeUUID));
        emitter.onError(error -> clustersContainer.getChainConsumers().removeNode(nodeUUID));
        emitter.onCompletion(() -> clustersContainer.getChainConsumers().removeNode(nodeUUID));

        // Returns the ChainSend and BlockSend SSE
        return emitter;
    }

    // Publishes a SendChainToConsumerEvent with the chain to the node that needs it
    @PostMapping("/chaingive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity chainGive(@RequestBody SerializableChain chain, @RequestParam("consumer") String consumerUUID)
    {
        if (!isValidUUID(consumerUUID)) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Invalid Consumer UUID"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try
        {
            // Send chain through chainconsumer stream SSE
            SendChainToConsumerEvent sendChainEvent = new SendChainToConsumerEvent(consumerUUID, chain);
            eventPublisher.publishEvent(sendChainEvent);
        }
        catch (Exception Ex)
        {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Invalid chain or consumer UUID"),
                    HttpStatus.BAD_REQUEST
            );
        }

        //JsonUtil jsonUtil = new JsonUtil();
        //System.out.println(jsonUtil.createJson(chain));

        return new ResponseEntity<>(
                new ApiResponse(true, "Chain was successfully sent"),
                HttpStatus.ACCEPTED
        );
    }

    // Publishes a GetChainFromProviderEvent with the node uuid that needs chain
    @GetMapping("/chainget")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity ChainGet(@RequestParam("consumeruuid") String consumerUUID)
    {
        // If the consumer uuid is invalid or not in consumers list
        if (!isValidUUID(consumerUUID) || !clustersContainer.getChainConsumers().existsInCluster(consumerUUID))
        {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Invalid consumer UUID or doesn't exist"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Remove consumer from chain providers list
        clustersContainer.getChainProviders().removeNode(consumerUUID);

        try
        {
            // Get chain from a provider
            GetChainFromProviderEvent chainFromProviderEvent = new GetChainFromProviderEvent(consumerUUID);
            eventPublisher.publishEvent(chainFromProviderEvent);
        }
        catch (Exception Ex)
        {
            return new ResponseEntity<>(
                new ApiResponse(false, "Invalid consumer UUID or doesn't exist"),
                HttpStatus.BAD_REQUEST
            );
        }


        return new ResponseEntity<>(
                new ApiResponse(true, "ChainGet request successfully sent"),
                HttpStatus.ACCEPTED
        );
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



    @EventListener
    protected void getChainFromProvider(GetChainFromProviderEvent event) throws IOException
    {
        // The passed on consumer UUID from chainGet() where the event is published
        String consumerUUID = event.getConsumerUUID();

        SseEmitter consumerEmitter = clustersContainer.getChainConsumers().getNodeEmitter(consumerUUID);
        String consumerNetworkUUID = clustersContainer.getChainConsumers().getNode(consumerUUID).getNetworkUUID();
        List<String> consumerNetworkProviders = new ArrayList<>();

        // Find a provider in the same network as the consumer
        for (Map.Entry<String, Node> nodeEntry : clustersContainer.getChainProviders().getCluster().entrySet())
        {
            // If a provider is found with the same NetworkUUID as the consumer NetworkUUID (so both are in same network)
            if (nodeEntry.getValue().getNetworkUUID().equals(consumerNetworkUUID))
            {
                // Add the provider UUID to the consumerNetworkProviders List
                consumerNetworkProviders.add(nodeEntry.getKey());
            }
        }


        if (consumerNetworkProviders.isEmpty()) // If no provider was found
        {
            // Send response to consumer
            consumerEmitter.send(new ApiResponse(false, "No provider available in your network"));
        }
        else
        {
            // Todo: Check if first provider has a valid chain, if not then choose another, and so...

            // Get first provider from list
            String providerUUID = consumerNetworkProviders.get(0);

            // Get provider emitter
            SseEmitter providerEmitter = clustersContainer.getChainProviders().getNodeEmitter(providerUUID);

            // Send a ChainRequest SSE through ChainProviders stream that contains the consumerUUID to the provider
            providerEmitter.send(consumerUUID, MediaType.APPLICATION_JSON);
        }
    }

    @EventListener
    protected void sendChainToConsumer(SendChainToConsumerEvent event) throws IOException
    {
        SerializableChain chain = event.getChain();
        String consumerUUID = event.getConsumerUUID();

        // Get consumer with consumerUUID from consumers
        Node consumerNode = clustersContainer.getChainConsumers().getCluster().get(consumerUUID);

        SseEmitter consumerEmitter = consumerNode.getEmitter();

        // Send the chain through the ChainConsumers SSE stream to the consumer with the consumerUUID
        consumerEmitter.send(chain, MediaType.APPLICATION_JSON);
    }

    @EventListener
    protected void SseKeepAlive(SseKeepAliveEvent event)
    {
        event.setKeepAliveData("0"); // Keep-Alive fake data

        clustersContainer.getChainProviders().getCluster().forEach((uuid, node) -> {
            try
            {
                System.out.println("Sending Keep-Alive Event to provider node: " + uuid);
                // Send fake data every 4 minutes to keep the connection alive
                node.getEmitter().send(event.getKeepAliveData(), MediaType.APPLICATION_JSON);
            }
            catch (IOException Ex) {
                clustersContainer.getChainProviders().removeNode(uuid);
                logger.error(Ex.getMessage());
            }
        });
        clustersContainer.getChainConsumers().getCluster().forEach((uuid, node) -> {
            try
            {
                node.getEmitter().send(event.getKeepAliveData(), MediaType.APPLICATION_JSON);
            }
            catch (IOException Ex) {
                clustersContainer.getChainConsumers().removeNode(uuid);
                logger.error(Ex.getMessage());
            }
        });
    }



    private boolean isValidUUID(String uuid)
    {
        if (uuid.isEmpty()) {
            return false;
        }

        try {
            UUID uuidFromString = UUID.fromString(uuid);
        }
        catch (IllegalArgumentException Ex) {
            return false;
        }

        return true;
    }

}
