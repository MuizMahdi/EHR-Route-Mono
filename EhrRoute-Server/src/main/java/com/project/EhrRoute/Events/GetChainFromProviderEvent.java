package com.project.EhrRoute.Events;
import org.springframework.context.ApplicationEvent;

public class GetChainFromProviderEvent extends ApplicationEvent
{
    private String consumerUUID;

    public GetChainFromProviderEvent(String consumerUUID) {
        super(consumerUUID);
        this.consumerUUID = consumerUUID;
    }

    public String getConsumerUUID() {
        return consumerUUID;
    }
    public void setConsumerUUID(String consumerUUID) {
        this.consumerUUID = consumerUUID;
    }
}
