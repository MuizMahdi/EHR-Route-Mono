package com.project.EhrRoute.Security;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.EhrRoute.Entities.Auth.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
*   This is the class whose instances will be returned from the custom UserDetailsService.
*   Spring Security will use the information stored in the UserPrincipal object to perform
*   authentication and authorization.
*/

@Component
public class UserPrincipal implements UserDetails
{
    private Long id;
    private String address;
    @JsonIgnore private String email;
    @JsonIgnore private String password;
    private Collection<? extends GrantedAuthority> authorities;


    public UserPrincipal() { }
    public UserPrincipal(Long id, String address, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.address = address;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }


    // CREATE METHOD
    // Returns a UserPrincipal object that has the user details/info

    public UserPrincipal create(User user)
    {
        // Create a list of GrantedAuthorities from the user roles
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
            new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());

        return new UserPrincipal(
            user.getId(),
            user.getAddress(),
            user.getEmail(),
            user.getPassword(),
            authorities
        );
    }
    

    // IMPLEMENTATION METHODS
    // UserDetails interface methods that Spring uses for authentication and authorization

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return address;
    } // returns base58 address which is used for authentication

    public String getAddress() {
        return getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        UserPrincipal Object = (UserPrincipal) obj;
        return Objects.equals(id, Object.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public Long getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
}
