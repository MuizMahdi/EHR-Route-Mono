package com.project.EhrRoute.Events;
import org.springframework.context.ApplicationEvent;


public class SendChainToConsumerEvent extends ApplicationEvent
{
    private String consumerUUID;
    private String chainUri;

    public SendChainToConsumerEvent(String consumerUUID, String chainUri) {
        super(chainUri);
        this.consumerUUID = consumerUUID;
        this.chainUri = chainUri;
    }

    public String getChainUri() {
        return chainUri;
    }
    public String getConsumerUUID() {
        return consumerUUID;
    }
    public void setChainUri(String chainUri) {
        this.chainUri = chainUri;
    }
    public void setConsumerUUID(String consumerUUID) {
        this.consumerUUID = consumerUUID;
    }
}
