package com.project.EMRChain.Events;
import org.springframework.context.ApplicationEvent;

public class SendChainToConsumerEvent extends ApplicationEvent
{
    private String consumerUUID;

    public SendChainToConsumerEvent(String consumerUUID) {
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
