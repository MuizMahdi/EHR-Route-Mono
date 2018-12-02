package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Repositories.NetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NetworkService
{
    private NetworkRepository networkRepository;

    @Autowired
    public NetworkService(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }


    public Network findByNetUUID(String netUUID) {
        return networkRepository.findByNetworkUUID(netUUID).orElse(null);
    }

    public void saveNetwork(Network network) {
        networkRepository.save(network);
    }
}
