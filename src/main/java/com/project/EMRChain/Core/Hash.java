package com.project.EMRChain.Core;
import com.project.EMRChain.Core.Utilities.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Hash
{
    private byte[] data;
    private HashUtil hashUtil;

    @Autowired
    public Hash(HashUtil hashUtil)
    {
        this.hashUtil = hashUtil;
    }

    public Hash(byte[] data) {
        this.data = data;
    }

    public byte[] hashData() {
        return hashUtil.SHA256(data);
    }

    public String getString() {
        return hashUtil.toString(data);
    }
}
