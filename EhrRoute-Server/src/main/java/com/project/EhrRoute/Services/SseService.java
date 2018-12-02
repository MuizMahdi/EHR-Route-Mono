package com.project.EhrRoute.Services;
import com.project.EhrRoute.Events.SseKeepAliveEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class SseService
{
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public SseService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }


    // Send an event every 4 minutes to keep the connection alive
    @Scheduled(fixedRate = 240000)
    public void SseKeepAlive()
    {
        SseKeepAliveEvent event = new SseKeepAliveEvent("0");
        this.eventPublisher.publishEvent(event);
    }
}
