package com.project.EMRChain.Core;
import com.project.EMRChain.Core.Utilities.AddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.PublicKey;

@Component
public class Address
{
    private String address;
    private AddressUtil addressUtil;

    @Autowired
    public Address(AddressUtil addressUtil)
    {
        this.addressUtil = addressUtil;
    }

    public Address() {}

    public void generateAddress(PublicKey publicKey)
    {
        this.address = addressUtil.generateAddress(publicKey);
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
