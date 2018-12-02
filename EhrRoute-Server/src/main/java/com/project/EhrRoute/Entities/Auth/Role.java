package com.project.EhrRoute.Entities.Auth;
import com.project.EhrRoute.Models.RoleName;
import org.hibernate.annotations.NaturalId;
import javax.persistence.*;

@Entity
@Table(name = "Role")
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private RoleName name;


    public  Role() { }
    public Role(RoleName name) {
        this.name = name;
    }


    public Long getId() {
        return id;
    }
    public RoleName getName() {
        return name;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setName(RoleName name) {
        this.name = name;
    }
}
