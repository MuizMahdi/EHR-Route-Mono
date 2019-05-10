package com.project.EhrRoute.Payload.Core;
import java.util.Set;

public class UserNetworksResponse
{
    private Set<NetworkResponse> userNetworks;

    public UserNetworksResponse() { }
    public UserNetworksResponse(Set<NetworkResponse> userNetworks) {
        this.userNetworks = userNetworks;
    }

    public Set<NetworkResponse> getUserNetworks() {
        return userNetworks;
    }
    public void setUserNetworks(Set<NetworkResponse> userNetworks) {
        this.userNetworks = userNetworks;
    }
}
