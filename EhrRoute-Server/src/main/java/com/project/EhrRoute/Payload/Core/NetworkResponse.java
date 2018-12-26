package com.project.EhrRoute.Payload.Core;

public class NetworkResponse
{
    private String networkUUID;
    private String chainRoot;
    private String name;

    public NetworkResponse(String name, String networkUUID, String chainRoot) {
        this.networkUUID = networkUUID;
        this.chainRoot = chainRoot;
        this.name = name;
    }


    public String getName() {
        return name;
    }
    public String getChainRoot() {
        return chainRoot;
    }
    public void setName(String name) {
        this.name = name;
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
