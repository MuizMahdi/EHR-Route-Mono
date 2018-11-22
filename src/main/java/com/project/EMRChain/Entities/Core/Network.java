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

    public Network() {}
    public Network(String networkUUID) {
        this.networkUUID = networkUUID;
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public void setNetworkUUID(String networkUUID) {
        this.networkUUID = networkUUID;
    }
}
