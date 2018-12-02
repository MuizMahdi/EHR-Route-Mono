package com.project.EhrRoute.Utilities;
import com.project.EhrRoute.Core.Node;
import com.project.EhrRoute.Events.GetChainFromProviderEvent;
import com.project.EhrRoute.Exceptions.BadRequestException;
import com.project.EhrRoute.Services.ClustersContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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

    public void fetchChainForNode(String nodeUUID)
    {
        // Remove node from chain providers cluster if its in it
        if (clustersContainer.getChainProviders().existsInCluster(nodeUUID)) {
            clustersContainer.getChainProviders().removeNode(nodeUUID);
        }

        // Add node to chain consumers cluster if its not in it
        if (!clustersContainer.getChainConsumers().existsInCluster(nodeUUID))
        {
            SseEmitter emitter = new SseEmitter(2592000000L);
            Node consumerNode = new Node(emitter, nodeUUID);
            clustersContainer.getChainConsumers().addNode(nodeUUID, consumerNode);
        }

        // Get chain from a provider
        try
        {
            GetChainFromProviderEvent chainFromProviderEvent = new GetChainFromProviderEvent(nodeUUID);
            eventPublisher.publishEvent(chainFromProviderEvent);
        }
        catch (Exception Ex)
        {
            throw new BadRequestException("Invalid consumer UUID or doesn't exist, chain get request wasn't sent");
        }
    }
}
