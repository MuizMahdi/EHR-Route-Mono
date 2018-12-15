package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.Core.ChainRoot;
import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Repositories.NetworkRepository;
import com.project.EhrRoute.Utilities.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NetworkService
{
    private NetworkRepository networkRepository;
    private UuidUtil uuidUtil;

    @Autowired
    public NetworkService(NetworkRepository networkRepository, UuidUtil uuidUtil) {
        this.networkRepository = networkRepository;
        this.uuidUtil = uuidUtil;
    }


    public Network findByNetUUID(String netUUID) {
        return networkRepository.findByNetworkUUID(netUUID).orElse(null);
    }

    public void saveNetwork(Network network) {
        networkRepository.save(network);
    }
}
