package com.project.EhrRoute.Payload.Core.SSEs;

import com.project.EhrRoute.Payload.Core.SerializableBlock;

public class BlockResponse
{
    private SerializableBlock block;
    private BlockMetadata metadata;

    public BlockResponse() { }
    public BlockResponse(SerializableBlock block, BlockMetadata metadata) {
        this.block = block;
        this.metadata = metadata;
    }

    public SerializableBlock getBlock() {
        return block;
    }
    public BlockMetadata getMetadata() {
        return metadata;
    }
    public void setBlock(SerializableBlock block) {
        this.block = block;
    }
    public void setMetadata(BlockMetadata metadata) {
        this.metadata = metadata;
    }
}
