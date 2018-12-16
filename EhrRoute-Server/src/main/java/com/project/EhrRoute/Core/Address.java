package com.project.EhrRoute.Core;
import com.project.EhrRoute.Core.Utilities.AddressUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;
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

    // Generates a Base58Check encoded address from public key
    public void generateAddress(PublicKey publicKey) throws GeneralSecurityException
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
