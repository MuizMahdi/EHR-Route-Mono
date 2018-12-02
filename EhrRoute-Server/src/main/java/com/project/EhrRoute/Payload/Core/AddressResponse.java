package com.project.EhrRoute.Payload.Core;

public class AddressResponse
{
    private String address;
    private String publicKey;
    private String privateKey;

    public AddressResponse(String address, String publicKey, String privateKey) {
        this.address = address;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getAddress() {
        return address;
    }
    public String getPublicKey() {
        return publicKey;
    }
    public String getPrivateKey() {
        return privateKey;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
