package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.Auth.Role;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Entities.Core.Network;
import com.project.EhrRoute.Exceptions.BadRequestException;
import com.project.EhrRoute.Exceptions.InternalErrorException;
import com.project.EhrRoute.Exceptions.NullUserNetworkException;
import com.project.EhrRoute.Exceptions.ResourceNotFoundException;
import com.project.EhrRoute.Models.RoleName;
import com.project.EhrRoute.Models.UuidSourceType;
import com.project.EhrRoute.Payload.Auth.SignUpRequest;
import com.project.EhrRoute.Payload.Auth.UserInfo;
import com.project.EhrRoute.Repositories.RoleRepository;
import com.project.EhrRoute.Repositories.UserRepository;
import com.project.EhrRoute.Utilities.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


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
    public UserInfo getUserInfo(Long userID) {
        // Get user
        User user = findUserById(userID);

        // Get a string list of user roles names
        List<String> userRoles = user.getRoles().stream().map(Role::getName).map(RoleName::toString).collect(Collectors.toList());

        return new UserInfo(
            userID,
            user.getAddress(),
            user.getEmail(),
            userRoles,
            user.isFirstLogin(),
            user.hasAddedInfo()
        );
    }


    @Transactional
    public User findUserByUsernameOrEmail(String usernameOrEmail) {
        User user = userRepository.findByUsernameOrEmail(
                usernameOrEmail, usernameOrEmail
        ).orElse(null);

        return user;
    }


    @Transactional
    public User findUserByAddressOrEmail(String addressOrEmail) {
        User user = userRepository.findByAddressOrEmail(addressOrEmail, addressOrEmail).orElseThrow(
            () -> new ResourceNotFoundException("User", "address/email", addressOrEmail)
        );

        return user;
    }


    @Transactional
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
            new ResourceNotFoundException("User", "userID", id)
        );
    }


    @Transactional
    public User findUserByAddress(String address) {
        return userRepository.findByAddress(address).orElseThrow(() ->
            new ResourceNotFoundException("User", "address", address)
        );
    }


    @Transactional
    public boolean isValidUserUsername(String username) {
        return findUserByUsernameOrEmail(username) != null;
    }


    @Transactional
    public boolean isUserFirstLogin(Long id)
    {
        return userRepository.getIsUserFirstLogin(id);
    }


    @Transactional
    public void setUserHasAddedInfo(Long userID) {
        User user = findUserById(userID);
        user.setHasAddedInfo(true);
        saveUser(user);
    }


    @Transactional
    public Set<Role> findUserRoles(Long id) {
        // Find user by username
        User user = userRepository.findById(id).orElseThrow(() ->
           new ResourceNotFoundException("User", "ID", id)
        );

        // Return user roles
        return user.getRoles();
    }


    // Checks if the user has a specific role
    public boolean userHasRole(User user, RoleName role) {
        Set<Role> userRoles = user.getRoles();

        for (Role userRole : userRoles) {
            if (userRole.getName() == role) {
                return true;
            }
        }

        return false;
    }


    @Transactional
    public Set<Network> findUserNetworks(User user) {
        //Network userNetwork = user.getNetwork();

        Set<Network> userNetworks = user.getNetworks();

        if (userNetworks == null || userNetworks.isEmpty()) {
            throw new ResourceNotFoundException("Networks", "user with address", user.getAddress());
        }

        return userNetworks;
    }


    @Transactional
    public void addUserNetwork(User user, Network network) {

        if (user == null || network == null) {
            throw new BadRequestException("Invalid network or user");
        }

        user.addNetwork(network);

        userRepository.save(user);
    }


    @Transactional
    public boolean userHasNetwork(User user, Network network) {
        if (user == null || network == null) {
            throw new BadRequestException("Invalid network or user");
        }

        // Iterate through user networks and check if network exists
        for (Network userNetwork : user.getNetworks()) {
            if (userNetwork.equals(network)) {
                return true;
            }
        }

        return false;
    }


    @Transactional
    public List<String> searchUsername(String usernameKeyword) {
        return userRepository.searchUsernamesByUsername(usernameKeyword);
    }


    @Transactional
    public List<String> searchProviderUsername(String usernameKeyword) {
        return userRepository.searchProvidersUsernamesByUsername(usernameKeyword);
    }


    @Transactional
    public User getUserByProviderUUID(String providerUUID) {
        return userRepository.findUserByProviderUUID(providerUUID).orElseThrow(() ->
            new ResourceNotFoundException("You are not a provider. User", "Provider UUID", providerUUID)
        );
    }
}
