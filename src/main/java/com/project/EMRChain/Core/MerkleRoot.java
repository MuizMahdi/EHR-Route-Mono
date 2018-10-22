package com.project.EMRChain.Core;

import com.project.EMRChain.Core.Utilities.Base58;

public class MerkleRoot
{
    private byte[] merkleRoot;

    public MerkleRoot() { }
    public MerkleRoot(byte[] merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    public byte[] getMerkleRoot() {
        return merkleRoot;
    }
    public String getBase58Hash(){
        return Base58.encode(merkleRoot);
    }
    public void setMerkleRoot(byte[] merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

}
