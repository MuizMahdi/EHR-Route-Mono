package com.project.EhrRoute.Security;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.ArrayList;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppUserDetailsServiceTest
{
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPrincipal userPrincipal;

    @InjectMocks
    private AppUserDetailsService userDetailsService = new AppUserDetailsService();

    @Test
    public void testLoadUserByUsername()
    {
        User user = mock(User.class);

        UserPrincipal aUserPrincipal = new UserPrincipal(1L, "Name", "Username", "Email", "Password", new ArrayList<GrantedAuthority>());

        when(userPrincipal.create(user)).thenReturn(aUserPrincipal);
    }

    @Test
    public void testLoadUserById()
    {
        User user = mock(User.class);
        when(user.getId()).thenReturn(123L);

        Optional<User> userOptional = Optional.of(user);
        when(userRepository.findById(user.getId())).thenReturn(userOptional);

        UserPrincipal aUserPrincipal = new UserPrincipal(123L, "Name", "Username", "Email", "Password", new ArrayList<GrantedAuthority>());

        when(userPrincipal.create(user)).thenReturn(aUserPrincipal);

        UserDetails resultUserDetails = userDetailsService.loadUserById(user.getId());
        assertEquals(resultUserDetails.getUsername(), aUserPrincipal.getUsername());
        assertEquals(resultUserDetails.getUsername(), "Username");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByIdReturnsException()
    {
        User user = mock(User.class);
        userDetailsService.loadUserById(user.getId());
    }
}
