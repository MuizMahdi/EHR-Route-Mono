package com.project.EMRChain.Services;
import com.project.EMRChain.Entities.Core.ChainRoot;
import com.project.EMRChain.Entities.Core.Network;
import com.project.EMRChain.Exceptions.ResourceNotFoundException;
import com.project.EMRChain.Repositories.ChainRootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChainRootService
{
    private ChainRootRepository chainRootRepository;
    private NetworkService networkService;

    @Autowired
    public ChainRootService(NetworkService networkService, ChainRootRepository chainRootRepository) {
        this.networkService = networkService;
        this.chainRootRepository = chainRootRepository;
    }


    // Returns true if the network's current chain root equals chainRoot
    public boolean checkNetworkChainRoot(String networkUUID, String chainRoot)
    {
        Network network = networkService.findByNetUUID(networkUUID);

        if (network == null) {
            return false;
        }

        ChainRoot networkCurrentChainRoot = chainRootRepository.findByNetwork(network).orElse(null);

        if (networkCurrentChainRoot == null) {
            return false;
        }

        String root = networkCurrentChainRoot.getRoot();

        if (root.isEmpty() || !root.equals(chainRoot)) {
            return  false;
        }

        return true;
    }

}
