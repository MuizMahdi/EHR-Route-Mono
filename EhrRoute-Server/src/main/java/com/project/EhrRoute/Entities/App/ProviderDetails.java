package com.project.EhrRoute.Entities.App;
import com.project.EhrRoute.Entities.Auth.User;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "providers_details")
public class ProviderDetails
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    private String providerUUID;

    @NotNull
    private String providerAddress;

    @OneToOne(targetEntity = Institution.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id", nullable = false)
    private Institution providerInstitution;

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
    public Institution getProviderInstitution() {
        return providerInstitution;
    }
    public String getProviderUUID() {
        return providerUUID;
    }
    public String getProviderAddress() {
        return providerAddress;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setProviderInstitution(Institution providerInstitution) {
        this.providerInstitution = providerInstitution;
    }
    public void setProviderUUID(String providerUUID) {
        this.providerUUID = providerUUID;
    }
    public void setProviderAddress(String providerAddress) {
        this.providerAddress = providerAddress;
    }
}
