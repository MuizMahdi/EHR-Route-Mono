package com.project.EhrRoute.Controllers;
import com.dropbox.core.*;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.sharing.CreateSharedLinkWithSettingsErrorException;
import com.dropbox.core.v2.sharing.ListSharedLinksResult;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.dropbox.core.v2.users.FullAccount;
import com.project.EhrRoute.Core.Node;
import com.project.EhrRoute.Events.GetChainFromProviderEvent;
import com.project.EhrRoute.Events.SendChainToConsumerEvent;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Payload.Core.ChainFetchRequest;
import com.project.EhrRoute.Payload.Core.SerializableChain;
import com.project.EhrRoute.Services.ClustersContainer;
import com.project.EhrRoute.Utilities.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@RestController
@RequestMapping("/chain")
public class ChainController
{
    private final Logger logger = LoggerFactory.getLogger(ChainController.class);

    @Value("${app.dropbox.accessToken}")
    private String dbxAccessToken;

    private ApplicationEventPublisher eventPublisher;
    private ClustersContainer clustersContainer;
    private UuidUtil uuidUtil;

    @Autowired
    public ChainController(UuidUtil uuidUtil, ClustersContainer clustersContainer, ApplicationEventPublisher eventPublisher) {
        this.uuidUtil = uuidUtil;
        this.eventPublisher = eventPublisher;
        this.clustersContainer = clustersContainer;
    }


    // Publishes a SendChainToConsumerEvent with the chain download uri to the node that needs it
    @RequestMapping(method = RequestMethod.POST, consumes = { "multipart/form-data" })
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity chainSend(@RequestPart("file") MultipartFile chainFile, @RequestParam("consumeruuid") String consumerUUID, @RequestParam("networkuuid") String networkUUID) throws DbxException, IOException
    {
        // Validate Consumer's UUID
        if (!uuidUtil.isValidUUID(consumerUUID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse(false, "Invalid Consumer UUID")
            );
        }

        // Construct a Dropbox request config
        DbxRequestConfig config = new DbxRequestConfig("EhrRoute/1.0");

        // Get the Dropbox client
        DbxClientV2 client = new DbxClientV2(config, dbxAccessToken);

        // Get file input stream to upload it
        InputStream in = chainFile.getInputStream();

        // Construct file path
        String filePath = "/" + networkUUID + ".chain";

        // Upload the chain file
        try {
            client.files().uploadBuilder(filePath).uploadAndFinish(in);
        }
        catch (UploadErrorException Ex) {
            // If file already exists/uploaded, then ignore the exception
        }

        SharedLinkMetadata meta;

        // Get download URL
        try {
            // Create a sharing link from filepath
            meta = client.sharing().createSharedLinkWithSettings(filePath);
        }
        catch (CreateSharedLinkWithSettingsErrorException Ex) {
            // If already exists, then get it
            ListSharedLinksResult link = client.sharing().listSharedLinksBuilder().withPath(filePath).start();
            meta = link.getLinks().get(0);
        }

        // Strip any other url params and append 'raw=1'
        String chainDownloadUri = meta.getUrl().split("\\?")[0] + "?raw=1";

        try {
            // Send the file uri through SSE to the consumer
            SendChainToConsumerEvent sendChainEvent = new SendChainToConsumerEvent(consumerUUID, chainDownloadUri);
            eventPublisher.publishEvent(sendChainEvent);
        }
        catch (Exception Ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse(false, "Invalid chain or consumer UUID")
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse(true, "File has been successfully sent")
        );
    }


    // Publishes a GetChainFromProviderEvent with the node uuid that needs chain from a certain network
    @GetMapping("/chainget")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity ChainGet(@RequestParam("consumeruuid") String consumerUUID, @RequestParam("netuuid") String networkUUID)
    {
        // If the consumer uuid is invalid or not in consumers list
        if (!uuidUtil.isValidUUID(consumerUUID) || !clustersContainer.getChainConsumers().existsInCluster(consumerUUID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse(false, "Invalid consumer UUID or doesn't exist")
            );
        }

        // Remove consumer from chain providers list
        clustersContainer.getChainProviders().removeNode(consumerUUID);

        try {
            // Get chain from a provider
            GetChainFromProviderEvent chainFromProviderEvent = new GetChainFromProviderEvent(consumerUUID, networkUUID);
            eventPublisher.publishEvent(chainFromProviderEvent);
        }
        catch (Exception Ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse(false, "Invalid consumer UUID or doesn't exist")
            );
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
            new ApiResponse(true, "A Chain request has been send to a node from you network")
        );
    }


    @EventListener
    protected void getChainFromProvider(GetChainFromProviderEvent event) throws IOException
    {
        // The passed on consumer UUID from chainGet() where the event is published
        String consumerUUID = event.getConsumerUUID();
        String consumerNetworkUUID = event.getNetworkUUID();

        // Get the consumer's SSE emitter using their UUID
        SseEmitter consumerEmitter = clustersContainer.getChainConsumers().getNodeEmitter(consumerUUID);

        // A list of UUIDs of providers in the consumer's network
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

        if (consumerNetworkProviders.isEmpty()) { // If no provider was found
            // Send response to consumer
            consumerEmitter.send(new ApiResponse(false, "No provider available in your network"));
        }
        else {
            // Todo: Check if first provider has a valid chain, if not then choose another, and so...

            // Get first provider from list
            String providerUUID = consumerNetworkProviders.get(0);

            // Construct a chain fetch request that will be sent to the provider via a SSE
            ChainFetchRequest fetchRequest = new ChainFetchRequest(consumerNetworkUUID, consumerUUID);

            // Get provider emitter
            SseEmitter providerEmitter = clustersContainer.getChainProviders().getNodeEmitter(providerUUID);

            SseEmitter.SseEventBuilder sseEvent = SseEmitter.event().data(fetchRequest).id("").name("chain-request");

            // Send a ChainRequest SSE through ChainProviders stream that contains the consumerUUID to the provider
            providerEmitter.send(sseEvent);
        }
    }


    @EventListener
    protected void sendChainToConsumer(SendChainToConsumerEvent event) throws IOException {
        String consumerUUID = event.getConsumerUUID();

        // Get consumer node with consumerUUID from consumers
        Node consumerNode = clustersContainer.getChainConsumers().getCluster().get(consumerUUID);

        // Send the chain uri through the SSE connection to the consumer
        SseEmitter.SseEventBuilder SSE = SseEmitter.event().data(event.getChainUri()).id("").name("chain-response");
        consumerNode.getEmitter().send(SSE);
    }
}
