package com.project.EhrRoute.Payload.App;
import java.util.List;


public class NetworkDetails
{
    private String networkUUID;
    private String networkName;
    private String networkRoot;
    private List<String> networkMembers;
    private List<String> networkInstitutions;


    public NetworkDetails() { }
    public NetworkDetails(String networkUUID, String networkName, String networkRoot, List<String> networkMembers, List<String> networkInstitutions) {
        this.networkUUID = networkUUID;
        this.networkName = networkName;
        this.networkRoot = networkRoot;
        this.networkMembers = networkMembers;
        this.networkInstitutions = networkInstitutions;
    }


    public String getNetworkName() {
        return networkName;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public String getNetworkRoot() {
        return networkRoot;
    }
    public List<String> getNetworkMembers() {
        return networkMembers;
    }
    public List<String> getNetworkInstitutions() {
        return networkInstitutions;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
    public void setNetworkRoot(String networkRoot) {
        this.networkRoot = networkRoot;
    }
    public void setNetworkMembers(List<String> networkMembers) {
        this.networkMembers = networkMembers;
    }
    public void setNetworkInstitutions(List<String> networkInstitutions) {
        this.networkInstitutions = networkInstitutions;
    }
}
