package com.project.EhrRoute.Payload.Core.SSEs;

public class BlockMetadata
{
    // The UUID of the consent request that resulted in this block's generation and broadcasting
    private String requestUUID;
    private String blockSource;

    public BlockMetadata() { }
    public BlockMetadata(String requestUUID) {
        this.requestUUID = requestUUID;
    }
    public BlockMetadata(String requestUUID, String blockSource) {
        this.requestUUID = requestUUID;
        this.blockSource = blockSource;
    }

    public String getRequestUUID() {
        return requestUUID;
    }
    public String getBlockSource() {
        return blockSource;
    }
    public void setRequestUUID(String requestUUID) {
        this.requestUUID = requestUUID;
    }
    public void setBlockSource(String blockSource) {
        this.blockSource = blockSource;
    }
}
