package com.project.EhrRoute.Payload.Core;
import com.project.EhrRoute.Payload.Core.SSEs.BlockResponse;


public class BlockFetchResponse
{
    private BlockResponse blockResponse;
    private String consumerUUID;
    private String networkUUID;
    private String networkChainRoot;

    public BlockFetchResponse() { }
    public BlockFetchResponse(BlockResponse blockResponse, String consumerUUID, String networkUUID, String networkChainRoot) {
        this.blockResponse = blockResponse;
        this.consumerUUID = consumerUUID;
        this.networkUUID = networkUUID;
        this.networkChainRoot = networkChainRoot;
    }

    public String getNetworkUUID() {
        return networkUUID;
    }
    public String getConsumerUUID() {
        return consumerUUID;
    }
    public String getNetworkChainRoot() {
        return networkChainRoot;
    }
    public BlockResponse getBlockResponse() {
        return blockResponse;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
    public void setConsumerUUID(String consumerUUID) {
        this.consumerUUID = consumerUUID;
    }
    public void setBlockResponse(BlockResponse blockResponse) {
        this.blockResponse = blockResponse;
    }
    public void setNetworkChainRoot(String networkChainRoot) {
        this.networkChainRoot = networkChainRoot;
    }
}
