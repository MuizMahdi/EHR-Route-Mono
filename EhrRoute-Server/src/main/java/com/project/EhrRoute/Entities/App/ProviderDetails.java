package com.project.EhrRoute.Entities.App;
import com.project.EhrRoute.Entities.Auth.User;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "providers_details")
public class ProviderDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String providerUUID;

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public ProviderDetails() { }
    public ProviderDetails(User user, @NotBlank String providerUUID) {
        this.user = user;
        this.providerUUID = providerUUID;
    }


    public Long getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public String getProviderUUID() {
        return providerUUID;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setProviderUUID(String providerUUID) {
        this.providerUUID = providerUUID;
    }
}
