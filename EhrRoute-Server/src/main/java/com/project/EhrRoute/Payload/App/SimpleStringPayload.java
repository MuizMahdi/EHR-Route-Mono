package com.project.EhrRoute.Payload.App;


public class SimpleStringPayload
{
    private String payload;

    public SimpleStringPayload() { }
    public SimpleStringPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }
    public void setPayload(String payload) {
        this.payload = payload;
    }
}
