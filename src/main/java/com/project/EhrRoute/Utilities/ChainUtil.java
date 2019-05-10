package com.project.EhrRoute.Utilities;
import com.project.EhrRoute.Core.Node;
import com.project.EhrRoute.Events.GetChainFromProviderEvent;
import com.project.EhrRoute.Exceptions.BadRequestException;
import com.project.EhrRoute.Exceptions.ResourceEmptyException;
import com.project.EhrRoute.Services.ClustersContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.Set;


@Component
public class ChainUtil
{
    private ClustersContainer clustersContainer;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public ChainUtil(ClustersContainer clustersContainer, ApplicationEventPublisher eventPublisher) {
        this.clustersContainer = clustersContainer;
        this.eventPublisher = eventPublisher;
    }


    public void fetchChainForNode(String nodeUUID, String networkUUID) throws ResourceEmptyException
    {
        // Get node networks set using node in providers cluster
        Set<String> nodeNetworks = clustersContainer.getChainProviders().getNode(nodeUUID).getNetworksUUIDs();

        // If node fetched from providers cluster has no networks
        if (nodeNetworks == null || nodeNetworks.isEmpty()) {
            // Fetch the same node from the consumers cluster
            if (clustersContainer.getChainConsumers().getNode(nodeUUID).getNetworksUUIDs() != null) {
                nodeNetworks = clustersContainer.getChainConsumers().getNode(nodeUUID).getNetworksUUIDs();
            }
            else {
                // If it still has no networks
                throw new ResourceEmptyException("Node has no networks");
            }
        }

        // Remove node from chain providers cluster if its in it
        if (clustersContainer.getChainProviders().existsInCluster(nodeUUID)) {
            clustersContainer.getChainProviders().removeNode(nodeUUID);
        }

        // Add node to chain consumers cluster if its not in it
        if (!clustersContainer.getChainConsumers().existsInCluster(nodeUUID)) {

            SseEmitter emitter = new SseEmitter(2592000000L);
            Node consumerNode = new Node(emitter, nodeNetworks);
            clustersContainer.getChainConsumers().addNode(nodeUUID, consumerNode);
        }

        // Get chain from a provider
        try {
            GetChainFromProviderEvent chainFromProviderEvent = new GetChainFromProviderEvent(nodeUUID, networkUUID);
            eventPublisher.publishEvent(chainFromProviderEvent);
        }
        catch (Exception Ex) {
            throw new BadRequestException("Invalid consumer UUID or doesn't exist, chain get request wasn't sent");
        }
    }
}
