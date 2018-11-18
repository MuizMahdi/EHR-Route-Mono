package com.project.EMRChain.Core;
import com.project.EMRChain.Core.Utilities.HashUtil;
import com.project.EMRChain.Core.Utilities.StringUtil;


public class Hash
{
    private byte[] data;

    public Hash(byte[] data) {
        this.data = data;
    }

    public byte[] hashData() {
        HashUtil hashUtil = new HashUtil();
        return hashUtil.SHA256(data);
    }

    public String getString() {
        StringUtil stringUtil = new StringUtil();
        return stringUtil.getStringFromBytes(data);
    }
}
