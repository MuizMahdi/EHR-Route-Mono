package com.project.EMRChain.Core;
import com.project.EMRChain.Core.Utilities.HashUtil;

public class Hash
{
    private byte[] data;

    public Hash(byte[] data) {
        this.data = data;
    }

    public byte[] hashData() {
        return HashUtil.SHA256(data);
    }

    public String getString() {
        return HashUtil.toString(data);
    }
}
