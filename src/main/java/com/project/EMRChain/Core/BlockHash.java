package com.project.EMRChain.Core;

import com.project.EMRChain.Core.Utilities.Base58;

public class BlockHash
{
    private byte[] blockHash;

    public BlockHash(byte[] blockHash) {
        this.blockHash = blockHash;
    }

    public byte[] getBlockHash() {
        return blockHash;
    }
    public String getBase58Hash() { return Base58.encode(blockHash); }
    public void setBlockHash(byte[] blockHash) {
        this.blockHash = blockHash;
    }
}
