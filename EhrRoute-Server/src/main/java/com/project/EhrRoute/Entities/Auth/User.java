package com.project.EhrRoute.Entities.Auth;
import com.project.EhrRoute.Audits.DateAudit;
import com.project.EhrRoute.Entities.Core.Network;
import org.hibernate.annotations.NaturalId;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})
})
public class User extends DateAudit
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max=40)
    private String name;

    @NotBlank
    @Size(max=15)
    private String username;

    @NaturalId
    @NotBlank
    @Email
    @Size(max=40)
    private String email;

    @NotBlank
    @Size(max=100)
    private String password;

    @Column(name = "enabled")
    private boolean isEnabled;

    @Column(name = "firstLogin")
    private boolean isNonFirstLogin;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_network",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "network_id")
    )
    private Set<Network> networks;

    public User() { }
    public User(@NotBlank @Size(max = 40) String name, @NotBlank @Size(max = 15) String username, @NotBlank @Email @Size(max = 40) String email, @NotBlank @Size(max = 100) String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.isEnabled = false;
        this.isNonFirstLogin = false;
    }

    public void addNetwork(Network network) {
        this.networks.add(network);
    }
    public void removeNetwork(Network network) {
        this.networks.remove(network);
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public Set<Role> getRoles() {
        return roles;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public boolean isEnabled() {
        return isEnabled;
    }
    public Set<Network> getNetworks() {
        return networks;
    }
    public boolean isNonFirstLogin() {
        return isNonFirstLogin;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setNetworks(Set<Network> networks) {
        this.networks = networks;
    }
    public void setNonFirstLogin(boolean firstLogin) {
        isNonFirstLogin = firstLogin;
    }
}
