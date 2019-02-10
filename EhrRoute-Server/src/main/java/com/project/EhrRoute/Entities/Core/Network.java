package com.project.EhrRoute.Entities.Core;
import com.project.EhrRoute.Entities.Auth.User;
import org.hibernate.annotations.NaturalId;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name="Network")
public class Network
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NaturalId
    private String networkUUID;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "network_root",
            joinColumns = @JoinColumn(name = "network_id"),
            inverseJoinColumns = @JoinColumn(name = "root_id")
    )
    private ChainRoot chainRoot;

    @ManyToMany(mappedBy = "networks")
    private Set<User> users = new HashSet<>();


    public Network() { }
    public Network(String networkUUID, ChainRoot chainRoot) {
        this.networkUUID = networkUUID;
        this.chainRoot = chainRoot;
    }
    public Network(String name, String networkUUID, ChainRoot chainRoot) {
        this.name = name;
        this.networkUUID = networkUUID;
        this.chainRoot = chainRoot;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Network)) return false;

        Network network = (Network) o;

        if (!id.equals(network.id)) return false;
        if (!name.equals(network.name)) return false;
        if (!networkUUID.equals(network.networkUUID)) return false;
        return chainRoot.equals(network.chainRoot);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + networkUUID.hashCode();
        result = 31 * result + chainRoot.hashCode();
        return result;
    }


    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Set<User> getUsers() {
        return users;
    }
    public ChainRoot getChainRoot() {
        return chainRoot;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public void setUsers(Set<User> users) {
        this.users = users;
    }
    public void setChainRoot(ChainRoot chainRoot) {
        this.chainRoot = chainRoot;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
}
