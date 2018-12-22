package com.project.EhrRoute.Events;
import org.springframework.context.ApplicationEvent;

public class GetChainFromProviderEvent extends ApplicationEvent
{
    private String consumerUUID;
    private String networkUUID;

    public GetChainFromProviderEvent(String consumerUUID, String networkUUID) {
        super(consumerUUID);
        this.consumerUUID = consumerUUID;
        this.networkUUID = networkUUID;
    }

    public String getNetworkUUID() { return networkUUID; }
    public String getConsumerUUID() {
        return consumerUUID;
    }
    public void setNetworkUUID(String networkUUID) { this.networkUUID = networkUUID; }
    public void setConsumerUUID(String consumerUUID) {
        this.consumerUUID = consumerUUID;
    }
}
