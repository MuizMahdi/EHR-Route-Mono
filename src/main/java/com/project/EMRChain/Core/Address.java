package com.project.EMRChain.Core;
import com.project.EMRChain.Core.Utilities.AddressUtil;
import java.security.PublicKey;

public class Address
{
    private String address;

    public Address() {}
    public Address(PublicKey publicKey) {
        this.address = AddressUtil.generateAddress(publicKey);
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}