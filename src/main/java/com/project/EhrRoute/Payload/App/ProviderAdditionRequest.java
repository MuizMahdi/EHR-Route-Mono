package com.project.EhrRoute.Payload.App;

public class ProviderAdditionRequest
{
    private String address;

    public ProviderAdditionRequest() { }
    public ProviderAdditionRequest(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
