package com.project.EhrRoute.Core.RTC;
import com.project.EhrRoute.Events.BlocksFetchRequestsCheckEvent;
import com.project.EhrRoute.Events.SseKeepAliveEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class NodesEventScheduler
{
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public NodesEventScheduler(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    /**
     * Sends an event every 30 seconds to keep the connection alive and to filter out disconnected nodes
     */
    @Scheduled(fixedRate = 30000)
    public void sendNodesHeartBeat() {
        SseKeepAliveEvent event = new SseKeepAliveEvent("0");
        eventPublisher.publishEvent(event);
    }

    /**
     * Sends an event every 5 seconds to check blocks fetch requests
     */
    @Scheduled(fixedRate = 5000)
    public void checkBlocksFetchRequests() {
        BlocksFetchRequestsCheckEvent event = new BlocksFetchRequestsCheckEvent();
        eventPublisher.publishEvent(event);
    }
}
