package com.project.EMRChain.Services;
import com.project.EMRChain.Entities.Core.Network;
import com.project.EMRChain.Repositories.NetworkRepository;
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
}
