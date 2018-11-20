package com.project.EMRChain.Payload.Core;

public class BlockAddition
{
    private SerializableBlock block;
    private String chainRootWithoutBlock;
    private String chainRootWithBlock;

    public BlockAddition(SerializableBlock block, String chainRootWithoutBlock, String chainRootWithBlock) {
        this.block = block;
        this.chainRootWithoutBlock = chainRootWithoutBlock;
        this.chainRootWithBlock = chainRootWithBlock;
    }

    public SerializableBlock getBlock() {
        return block;
    }
    public String getChainRootWithBlock() {
        return chainRootWithBlock;
    }
    public String getChainRootWithoutBlock() {
        return chainRootWithoutBlock;
    }

    public void setBlock(SerializableBlock block) {
        this.block = block;
    }
    public void setChainRootWithoutBlock(String chainRootWithoutBlock) {
        this.chainRootWithoutBlock = chainRootWithoutBlock;
    }
    public void setChainRootWithBlock(String chainRootWithBlock) {
        this.chainRootWithBlock = chainRootWithBlock;
    }
}
