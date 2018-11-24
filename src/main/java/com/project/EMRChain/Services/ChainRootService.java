package com.project.EMRChain.Services;
import com.project.EMRChain.Entities.Core.ChainRoot;
import com.project.EMRChain.Entities.Core.Network;
import com.project.EMRChain.Exceptions.BadRequestException;
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

        // If it doesn't exist in DB
        if (network == null) {
            throw new BadRequestException("Invalid Network");
        }

        if (chainRoot.isEmpty()) {
            throw new BadRequestException("Invalid Chain Root");
        }

        //ChainRoot networkCurrentChainRoot = chainRootRepository.findByNetwork(network).orElse(null);

        // Latest chain root of the network
        ChainRoot networkCurrentChainRoot = network.getChainRoot();

        if (networkCurrentChainRoot == null) {
            throw new BadRequestException("Network has no Chain Root");
        }

        String root = networkCurrentChainRoot.getRoot();

        if (!root.equals(chainRoot)) {
            return false;
        }

        return true;
    }

}
