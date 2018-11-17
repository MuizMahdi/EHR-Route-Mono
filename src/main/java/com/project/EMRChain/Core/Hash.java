package com.project.EMRChain.Core;
import com.project.EMRChain.Core.Utilities.HashUtil;
import com.project.EMRChain.Core.Utilities.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Hash
{
    private byte[] data;
    private HashUtil hashUtil;
    private StringUtil stringUtil;

    @Autowired
    public Hash(HashUtil hashUtil, StringUtil stringUtil) {
        this.hashUtil = hashUtil;
        this.stringUtil = stringUtil;
    }

    public Hash(byte[] data) {
        this.data = data;
    }

    public byte[] hashData() {
        return hashUtil.SHA256(data);
    }

    public String getString() {
        return stringUtil.getStringFromBytes(data);
    }
}
