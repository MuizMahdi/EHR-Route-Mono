package com.project.EhrRoute.Core;
import com.project.EhrRoute.Core.Utilities.HashUtil;
import com.project.EhrRoute.Core.Utilities.StringUtil;


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
