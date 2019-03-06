package com.project.EhrRoute.Payload.Core;


public class ChainFetchRequest
{
    private String networkUUID;
    private String consumerUUID;


    public ChainFetchRequest() { }
    public ChainFetchRequest(String networkUUID, String consumerUUID) {
        this.networkUUID = networkUUID;
        this.consumerUUID = consumerUUID;
    }


    public String getNetworkUUID() {
        return networkUUID;
    }
    public String getConsumerUUID() {
        return consumerUUID;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
    public void setConsumerUUID(String consumerUUID) {
        this.consumerUUID = consumerUUID;
    }
}
