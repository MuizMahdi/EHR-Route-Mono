package com.project.EhrRoute.Payload.App;


public class NetworkRootUpdate
{
    private String merkleRoot;
    private String networkUUID;
    private String consentRequestUUID;

    public NetworkRootUpdate() { }
    public NetworkRootUpdate(String merkleRoot, String networkUUID, String consentRequestUUID) {
        this.merkleRoot = merkleRoot;
        this.networkUUID = networkUUID;
        this.consentRequestUUID = consentRequestUUID;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public String getConsentRequestUUID() {
        return consentRequestUUID;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
    public void setConsentRequestUUID(String consentRequestUUID) {
        this.consentRequestUUID = consentRequestUUID;
    }
}


