package com.project.EMRChain.Entities.Core;
import org.hibernate.annotations.NaturalId;
import javax.persistence.*;

@Entity
@Table(name="Network")
public class Network
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private String networkUUID;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "network_root",
            joinColumns = @JoinColumn(name = "network_id"),
            inverseJoinColumns = @JoinColumn(name = "root_id")
    )
    private ChainRoot chainRoot;

    public Network() { }
    public Network(String networkUUID, ChainRoot chainRoot) {
        this.networkUUID = networkUUID;
        this.chainRoot = chainRoot;
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public ChainRoot getChainRoot() {
        return chainRoot;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public void setChainRoot(ChainRoot chainRoot) {
        this.chainRoot = chainRoot;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
}