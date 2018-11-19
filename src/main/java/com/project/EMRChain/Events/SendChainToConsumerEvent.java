package com.project.EMRChain.Events;
import com.project.EMRChain.Payload.Core.SerializableChain;
import org.springframework.context.ApplicationEvent;

public class SendChainToConsumerEvent extends ApplicationEvent
{
    private String consumerUUID;
    private SerializableChain chain;

    public SendChainToConsumerEvent(String consumerUUID, SerializableChain chain) {
        super(chain);
        this.consumerUUID = consumerUUID;
        this.chain = chain;
    }

    public SerializableChain getChain() {
        return chain;
    }
    public String getConsumerUUID() {
        return consumerUUID;
    }
    public void setChain(SerializableChain chain) {
        this.chain = chain;
    }
    public void setConsumerUUID(String consumerUUID) {
        this.consumerUUID = consumerUUID;
    }
}
