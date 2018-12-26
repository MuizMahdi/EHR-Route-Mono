package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Core.Node;
import com.project.EhrRoute.Events.GetChainFromProviderEvent;
import com.project.EhrRoute.Events.SendChainToConsumerEvent;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Payload.Core.SerializableChain;
import com.project.EhrRoute.Services.ClustersContainer;
import com.project.EhrRoute.Utilities.UuidUtil;
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
import java.util.*;


@RestController
@RequestMapping("/chain")
public class ChainController
{
    private final Logger logger = LoggerFactory.getLogger(ChainController.class);

    private ApplicationEventPublisher eventPublisher;
    private ClustersContainer clustersContainer;
    private UuidUtil uuidUtil;

    @Autowired
    public ChainController(UuidUtil uuidUtil, ClustersContainer clustersContainer, ApplicationEventPublisher eventPublisher) {
        this.uuidUtil = uuidUtil;
        this.eventPublisher = eventPublisher;
        this.clustersContainer = clustersContainer;
    }


    // Publishes a SendChainToConsumerEvent with the chain to the node that needs it
    @PostMapping("/chaingive")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity chainGive(@RequestBody SerializableChain chain, @RequestParam("consumer") String consumerUUID)
    {
        if (!uuidUtil.isValidUUID(consumerUUID)) {
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


    // Publishes a GetChainFromProviderEvent with the node uuid that needs chain from a certain network
    @GetMapping("/chainget")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity ChainGet(@RequestParam("consumeruuid") String consumerUUID, @RequestParam("netuuid") String networkUUID)
    {
        // If the consumer uuid is invalid or not in consumers list
        if (!uuidUtil.isValidUUID(consumerUUID) || !clustersContainer.getChainConsumers().existsInCluster(consumerUUID))
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
            GetChainFromProviderEvent chainFromProviderEvent = new GetChainFromProviderEvent(consumerUUID, networkUUID);
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


    @EventListener
    protected void getChainFromProvider(GetChainFromProviderEvent event) throws IOException
    {
        // The passed on consumer UUID from chainGet() where the event is published
        String consumerUUID = event.getConsumerUUID();
        String consumerNetworkUUID = event.getNetworkUUID();

        SseEmitter consumerEmitter = clustersContainer.getChainConsumers().getNodeEmitter(consumerUUID);
        //String consumerNetworkUUID = clustersContainer.getChainConsumers().getNode(consumerUUID).getNetworkUUID();

        List<String> consumerNetworkProviders = new ArrayList<>();

        // Go through the providers cluster and find providers in the same network as the consumer
        for (Map.Entry<String, Node> providerNode : clustersContainer.getChainProviders().getCluster().entrySet())
        {
            // if the provider is in the same network as consumer
            if (providerNode.getValue().getNetworksUUIDs().contains(consumerNetworkUUID)) {
                // Add the provider UUID to the consumerNetworkProviders List
                consumerNetworkProviders.add(providerNode.getKey());
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

        // Get consumer node with consumerUUID from consumers
        Node consumerNode = clustersContainer.getChainConsumers().getCluster().get(consumerUUID);

        SseEmitter consumerEmitter = consumerNode.getEmitter();

        // Send the chain through the ChainConsumers SSE stream to the consumer with the consumerUUID
        consumerEmitter.send(chain, MediaType.APPLICATION_JSON);
    }
}
