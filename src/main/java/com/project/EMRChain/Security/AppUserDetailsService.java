package com.project.EMRChain.Security;
import com.project.EMRChain.Entities.Auth.User;
import com.project.EMRChain.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/*
*   UserDetailsService is implemented in order to use any DAO class for authentication.
*   Once the DAO is configured, its loadUserByUsername() is used to validate the user.
*   It is used as a user DAO and is the strategy used by the DaoAuthenticationProvider.
*/

@Service
public class AppUserDetailsService implements UserDetailsService
{
    private UserRepository userRepository;

    @Autowired
    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(() ->
            new UsernameNotFoundException("User User not found with username or email: " + usernameOrEmail)
        );

        return UserPrincipal.create(user);
    }


    @Transactional
    public UserDetails loadUserById(Long id) { // Load by ID is used for JWT Authentication Filter

        User user = userRepository.findById(id).orElseThrow(() ->
            new UsernameNotFoundException("User not found with id: " + id)
        );

        return UserPrincipal.create(user);
    }

}