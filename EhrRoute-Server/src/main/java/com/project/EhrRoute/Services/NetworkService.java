package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Exceptions.NullUserNetworkException;
import com.project.EhrRoute.Exceptions.ResourceEmptyException;
import com.project.EhrRoute.Repositories.NetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Random;
import java.util.Set;


@Service
public class NetworkService
{
    private NetworkRepository networkRepository;

    @Autowired
    public NetworkService(NetworkRepository networkRepository) {
        this.networkRepository = networkRepository;
    }


    @Transactional
    public Network findByNetUUID(String netUUID) {
        return networkRepository.findByNetworkUUID(netUUID).orElse(null);
    }


    @Transactional
    public void saveNetwork(Network network) {
        networkRepository.save(network);
    }


    @Transactional
    public String getNetworkChainRoot(Network network) {
        return network.getChainRoot().getRoot();
    }


    @Transactional
    public String getNetworkUuidByName(String networkName) {
        return networkRepository.getNetworkUUIDByName(networkName).orElseThrow(() ->
            new NullUserNetworkException("A network with name " + networkName + " was not found")
        );
    }


    @Transactional
    public List<String> searchNetworksByName(String networkNameKeyword) {
        return networkRepository.searchNetworksByName(networkNameKeyword);
    }


    @Transactional
    public User getNetworkRandomMember(String networkUUID) throws NullUserNetworkException
    {
        // Get the network using the UUID
        Network network = findByNetUUID(networkUUID);

        // Validate network
        if (network == null) {
            throw new NullUserNetworkException("Network with UUID: " + networkUUID + ", was not found");
        }

        // Get network's users set
        Set<User> networkUsers = network.getUsers();
        int networkUsersSize = networkUsers.size();

        // Validate network size
        if (networkUsersSize < 1) {
            throw new ResourceEmptyException("Network with UUID: " + networkUUID + ", has no members");
        }

        // Pick a random number between 0 and the network's users set size
        int rand = new Random().nextInt(networkUsersSize);
        int i = 0;

        // Pick a random member from the users
        for(User user : networkUsers) {
            if (i == rand) return user;
            i++;
        }

        // If couldn't pick a random user
        return null;
    }
}
