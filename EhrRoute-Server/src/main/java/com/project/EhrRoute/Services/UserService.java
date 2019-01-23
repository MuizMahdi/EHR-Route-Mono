package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.Auth.Role;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Exceptions.BadRequestException;
import com.project.EhrRoute.Exceptions.InternalErrorException;
import com.project.EhrRoute.Exceptions.NullUserNetworkException;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import com.project.EhrRoute.Models.RoleName;
import com.project.EhrRoute.Payload.Auth.SignUpRequest;
import com.project.EhrRoute.Repositories.RoleRepository;
import com.project.EhrRoute.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Set;


@Service
public class UserService
{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Transactional
    public boolean userEmailExists(String email)
    {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User createUser(SignUpRequest request) {
        // Create user from request data
        User user = new User(
                request.getName(),
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );

        // Bcrypt hash the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Get the 'User' role
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() ->
            new InternalErrorException("User Role not set")
        );

        // Set the user role to 'User'
        user.setRoles(Collections.singleton(userRole));

        // Save user in DB
        User result = userRepository.save(user);

        return result;
    }

    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public User findUserByUsernameOrEmail(String usernameOrEmail) {
        User user = userRepository.findByUsernameOrEmail(
                usernameOrEmail, usernameOrEmail
        ).orElse(null);

        return user;
    }

    @Transactional
    public User findUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        return user;
    }

    @Transactional
    public Set<Role> findUserRoles(String username) throws ResourceNotFoundException {

        // Find user by username
        User user = userRepository.findByUsername(username).orElse(null);

        // If invalid username
        if (user == null) {
            throw new ResourceNotFoundException("User", "username", username);
        }

        // Return user roles
        return user.getRoles();
    }

    @Transactional
    public Set<Network> findUserNetworks(User user) throws NullUserNetworkException {
        //Network userNetwork = user.getNetwork();

        Set<Network> userNetworks = user.getNetworks();

        if (userNetworks == null || userNetworks.isEmpty()) {
            throw new NullUserNetworkException(
                "User with username: [" +
                user.getUsername() +
                "] is not registered in any network."
            );
        }

        return userNetworks;
    }

    @Transactional
    public void addUserNetwork(User user, Network network) throws BadRequestException {

        if (user == null || network == null) {
            throw new BadRequestException("Invalid network or user");
        }

        user.addNetwork(network);

        userRepository.save(user);
    }

    @Transactional
    public boolean userHasNetwork(User user, Network network) throws BadRequestException {

        boolean hasNetwork = false;

        if (user == null || network == null) {
            throw new BadRequestException("Invalid network or user");
        }

        // Iterate through user networks and check if network exists
        for (Network userNetwork : user.getNetworks()) {
            if (userNetwork.equals(network)) {
                hasNetwork = true;
            }
        }

        return hasNetwork;
    }

    @Transactional
    public List<String> searchUsername(String usernameKeyword) {
        return userRepository.searchUsernamesByUsername(usernameKeyword);
    }

    @Transactional
    public List<String> searchProviderUsername(String usernameKeyword) {
        System.out.println(userRepository.searchProvidersUsernamesByUsername(usernameKeyword).get(0));
        return userRepository.searchProvidersUsernamesByUsername(usernameKeyword);
    }
}
