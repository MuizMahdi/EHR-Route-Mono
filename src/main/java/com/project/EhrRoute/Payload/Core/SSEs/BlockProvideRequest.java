package com.project.EhrRoute.Payload.Core.SSEs;


public class BlockProvideRequest
{
    private String consumerUUID;
    private String networkUUID;
    private Long blockId;

    public BlockProvideRequest() { }
    public BlockProvideRequest(String consumerUUID, String networkUUID, Long blockId) {
        this.consumerUUID = consumerUUID;
        this.networkUUID = networkUUID;
        this.blockId = blockId;
    }

    public Long getBlockId() {
        return blockId;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public String getConsumerUUID() {
        return consumerUUID;
    }
    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
    public void setConsumerUUID(String consumerUUID) {
        this.consumerUUID = consumerUUID;
    }
}
