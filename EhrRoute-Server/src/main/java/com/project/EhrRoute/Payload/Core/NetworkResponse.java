package com.project.EhrRoute.Payload.Core;

public class NetworkResponse
{
    private String networkUUID;
    private String chainRoot;

    public NetworkResponse(String networkUUID, String chainRoot) {
        this.networkUUID = networkUUID;
        this.chainRoot = chainRoot;
    }

    public String getChainRoot() {
        return chainRoot;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public void setChainRoot(String chainRoot) {
        this.chainRoot = chainRoot;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
}
