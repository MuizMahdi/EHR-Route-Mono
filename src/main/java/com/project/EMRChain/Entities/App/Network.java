package com.project.EMRChain.Entities.App;
import org.hibernate.annotations.NaturalId;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="Network")
public class Network
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private String networkUUID;


    public Network() {
        this.networkUUID = UUID.randomUUID().toString();
    }


    public Long getId() {
        return id;
    }
    public String getNetworkUUID() {
        return networkUUID;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
