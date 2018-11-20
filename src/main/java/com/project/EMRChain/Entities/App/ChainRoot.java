package com.project.EMRChain.Entities.App;
import javax.persistence.*;

@Entity
@Table(name="ChainRoot")
public class ChainRoot
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String root;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "network_root",
            joinColumns = @JoinColumn(name = "root_id"),
            inverseJoinColumns = @JoinColumn(name = "network_id")
    )
    private Network network;

    public ChainRoot() { }
    public ChainRoot(String root) {
        this.root = root;
    }

    public Long getId() {
        return id;
    }
    public String getRoot() {
        return root;
    }
    public Network getNetwork() {
        return network;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setRoot(String root) {
        this.root = root;
    }
    public void setNetwork(Network network) {
        this.network = network;
    }
}
