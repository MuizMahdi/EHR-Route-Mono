package com.project.EMRChain.Entities.Core;
import javax.persistence.*;

@Entity
@Table(name="ChainRoot")
public class ChainRoot
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String root;

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
    public void setId(Long id) {
        this.id = id;
    }
    public void setRoot(String root) {
        this.root = root;
    }
}
