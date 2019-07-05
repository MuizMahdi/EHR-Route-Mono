package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.Core.ChainRoot;
import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ChainRootService
{
    private NetworkService networkService;

    @Autowired
    public ChainRootService(NetworkService networkService) {
        this.networkService = networkService;
    }


    // Returns true if the network's current chain root equals chainRoot
    public boolean isChainRootValid(String networkUUID, String chainRoot) {

        Network network = networkService.findNetwork(networkUUID);

        if (chainRoot.isEmpty()) {
            throw new BadRequestException("Invalid Chain Root");
        }

        // Latest chain root of the network
        ChainRoot networkCurrentChainRoot = network.getChainRoot();

        if (networkCurrentChainRoot == null || networkCurrentChainRoot.getRoot().isEmpty()) {
            throw new BadRequestException("Network has no Chain Root");
        }

        String root = networkCurrentChainRoot.getRoot();

        if (!root.equals(chainRoot)) {
            return false;
        }

        return true;
    }


    public void changeNetworkChainRoot(String networkUUID, String chainRoot) {

        Network network = networkService.findNetwork(networkUUID);

        if (chainRoot.isEmpty()) {
            throw new BadRequestException("Invalid Chain Root");
        }

        ChainRoot newChainRoot = new ChainRoot(chainRoot);

        network.setChainRoot(newChainRoot);

        networkService.saveNetwork(network);
    }

}
