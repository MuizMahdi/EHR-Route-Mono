package com.project.EMRChain.Controllers;
import com.project.EMRChain.Core.Node;
import com.project.EMRChain.Core.NodeCluster;
import com.project.EMRChain.Events.GetChainFromProviderEvent;
import com.project.EMRChain.Events.SendChainToConsumerEvent;
import com.project.EMRChain.Events.SseKeepAliveEvent;
import com.project.EMRChain.Payload.Auth.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@RestController
@RequestMapping("/chain")
public class ChainController
{
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public ChainController(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    private final Logger logger = LoggerFactory.getLogger(ChainController.class);
    private ExecutorService executorService = Executors.newCachedThreadPool();

    private NodeCluster chainProviders = new NodeCluster();
    private NodeCluster chainConsumers = new NodeCluster();



    @GetMapping("/chainprovider/{nodeuuid}/{netuuid}")
    public SseEmitter subscribeProvider(@PathVariable("nodeuuid") String nodeUUID, @PathVariable("netuuid") String networkUUID) throws IOException
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
            this.chainProviders.addNode(nodeUUID, node);

            System.out.println("Node with netUUID: " + this.chainProviders.getNode(nodeUUID).getNetworkUUID() + " Was added to ChainProviders");
        }


        // Remove the emitter on timeout/error/completion
        emitter.onTimeout(() -> {
            this.chainProviders.removeNode(nodeUUID);
            System.out.println("########## EMITTER TIMEOUT ##########");
        });

        emitter.onCompletion(() -> {
            this.chainProviders.removeNode(nodeUUID);
            System.out.println("######### EMITTER COMPLETION #########");
        });

        emitter.onError(error -> {
            this.chainProviders.removeNode(nodeUUID);
            System.out.println("########## EMITTER ERROR: " + error.getMessage() + " ##########");
        });

        // Returns the GetChainFromProviderEvent notification SSE
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
    public ResponseEntity chainGive(/* CHAIN REQUEST PAYLOAD RECEIVED HERE [Includes ConsumerUUID] */)
    {
        // Todo: 1. Check whether ConsumerUUID is valid or not
        // Todo: 2. Publish a SendChainToGetter event with the consumerUUID

        // Returns HttpStatus.OK on success
        return null;
    }

    @GetMapping("/chainget/{consumeruuid}")
    public ResponseEntity ChainGet(@PathVariable("consumeruuid") String consumerUUID)
    {
        // If the consumer uuid is invalid or not in consumers list
        if (!isValidUUID(consumerUUID) || !chainConsumers.existsInCluster(consumerUUID))
        {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Invalid consumer UUID or doesn't exist"),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Remove consumer from chain providers list
        this.chainProviders.removeNode(consumerUUID);

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


        // Returns HttpStatus.OK on success
        return new ResponseEntity<>(
                new ApiResponse(true, "ChainGet request successfully sent"),
                HttpStatus.OK
        );
    }


    // Called when client closes app (ngOnDestroy)
    @GetMapping("/close/{uuid}")
    public ResponseEntity closeConnection(@PathVariable("uuid") String uuid)
    {
        // Remove the client from clusters
        if(chainConsumers.existsInCluster(uuid)) {
            chainConsumers.removeNode(uuid);
        }

        if (chainProviders.existsInCluster(uuid)) {
            chainProviders.removeNode(uuid);
        }

        return new ResponseEntity<>(
                new ApiResponse(true, "Connection closed"),
                HttpStatus.OK
        );
    }



    @EventListener
    private void getChainFromProvider(GetChainFromProviderEvent event) throws IOException
    {
        // The passed on consumer UUID from chainGet() where the event is published
        String consumerUUID = event.getConsumerUUID();

        // Todo: Check if provider has a valid chain, if not then choose another, and so...

        // Get a provider node from the chain providers
        Map.Entry<String, Node> providerNode = this.chainProviders.getCluster().entrySet().iterator().next();

        // Get the provider's emitter
        SseEmitter providerEmitter = providerNode.getValue().getEmitter();

        // Send a ChainRequest SSE through ChainProviders stream that contains the consumerUUID to the provider
        providerEmitter.send(consumerUUID);
    }

    @EventListener
    private void sendChainToConsumer(SendChainToConsumerEvent event) throws IOException
    {
        String consumerUUID = event.getConsumerUUID();

        // Todo: Send a ChainGive response SSE which contains the chain through ChainConsumers stream
        // Todo: to the consumer in the consumer with the ConsumerUUID
    }

    @EventListener
    private void SseKeepAlive(SseKeepAliveEvent event)
    {
        event.setKeepAliveData("0"); // Keep-Alive fake data

        chainProviders.getCluster().forEach((uuid, node) -> {
            try
            {
                System.out.println("Sending Keep-Alive Event to provider node: " + uuid);
                // Send fake data every 4 minutes to keep the connection alive
                node.getEmitter().send(event.getKeepAliveData(), MediaType.APPLICATION_JSON);
            }
            catch (IOException Ex) {
                this.chainProviders.removeNode(uuid);
                logger.error(Ex.getMessage());
            }
        });
        chainConsumers.getCluster().forEach((uuid, node) -> {
            try
            {
                node.getEmitter().send(event.getKeepAliveData(), MediaType.APPLICATION_JSON);
            }
            catch (IOException Ex) {
                this.chainConsumers.removeNode(uuid);
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
