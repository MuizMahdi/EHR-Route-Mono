package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Exceptions.NullUserNetworkException;
import com.project.EhrRoute.Exceptions.ResourceEmptyException;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import com.project.EhrRoute.Payload.App.NetworkDetails;
import com.project.EhrRoute.Repositories.NetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class NetworkService
{
    private NetworkRepository networkRepository;
    private ProviderService providerService;

    @Autowired
    public NetworkService(NetworkRepository networkRepository, ProviderService providerService) {
        this.networkRepository = networkRepository;
        this.providerService = providerService;
    }


    @Transactional
    public Network findNetwork(String networkUUID) {
        return networkRepository.findByNetworkUUID(networkUUID).orElseThrow(() ->
            new NullUserNetworkException("Network with UUID " + networkUUID + " was not found")
        );
    }


    @Transactional
    public void saveNetwork(Network network) {
        networkRepository.save(network);
    }


    @Transactional
    private String getNetworkNameByUuid(String networkUUID) {
        return networkRepository.getNetworkNameByNetworkUUID(networkUUID).orElseThrow(() ->
            new NullUserNetworkException("Network name for a network with UUID " + networkUUID + " was not found")
        );
    }


    @Transactional
    public String getNetworkUuidByName(String networkName) {
        return networkRepository.getNetworkUUIDByName(networkName).orElseThrow(() ->
            new NullUserNetworkException("Network UUID for a network with name " + networkName + " was not found")
        );
    }


    @Transactional
    public List<String> searchNetworksByName(String networkNameKeyword) {
        return networkRepository.searchNetworksByName(networkNameKeyword);
    }


    @Transactional
    public String getNetworkChainRoot(String networkUUID) {
        return networkRepository.getNetworkChainRootByNetworkUUID(networkUUID).orElseThrow(() ->
            new NullUserNetworkException("Chain root for a network with UUID " + networkUUID + " was not found")
        );
    }


    @Transactional
    public User getNetworkRandomMember(String networkUUID) {

        // Get the network using the UUID
        Network network = findNetwork(networkUUID);

        // Get network's users set
        Set<User> networkUsers = network.getUsers();
        int networkUsersSize = networkUsers.size();

        // Validate network size
        if (networkUsersSize < 1 || networkUsers.isEmpty()) {
            throw new ResourceEmptyException("Network with UUID: " + networkUUID + ", has no members");
        }
        else if (networkUsersSize == 1) {
            for (User user : networkUsers) {
                return user;
            }
        }

        // Pick a random number between 0 and the network's users set size
        int rand = new Random().nextInt(networkUsersSize);
        int i = 0;

        // Pick a random member from the users
        for (User user : networkUsers) {
            if (i == rand) return user;
            i++;
        }

        // If couldn't pick a random user
        return null;
    }


    @Transactional
    private List<String> getNetworkInstitutions(String networkUUID) {

        List<String> networkMembersInstitutions = new ArrayList<>();

        Set<User> networkProviders = getNetworkMembers(networkUUID);

        networkProviders.forEach(user -> {

            try {
                networkMembersInstitutions.add(providerService.getProviderInstitution(user.getId()));
            }
            catch (ResourceNotFoundException Ex) {
                // If one of the network users is not a provider, has no provider details, or no institution, then simply ignore them.
            }

        });

        return networkMembersInstitutions;
    }


    @Transactional
    private List<String> getNetworkMembersNames(String networkUUID) {
        Set<User> networkMembers = getNetworkMembers(networkUUID);
        return networkMembers.stream().map(User::getName).collect(Collectors.toList());
    }


    @Transactional
    private Set<User> getNetworkMembers(String networkUUID) {
        // Get the network using the UUID
        Network network = findNetwork(networkUUID);
        return network.getUsers();
    }


    @Transactional
    public NetworkDetails getNetworkDetails(String networkUUID) {
        return new NetworkDetails(
            networkUUID,
            getNetworkNameByUuid(networkUUID),
            getNetworkChainRoot(networkUUID),
            getNetworkMembersNames(networkUUID),
            getNetworkInstitutions(networkUUID)
        );
    }
}
