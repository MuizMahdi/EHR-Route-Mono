package com.project.EMRChain.Security;
import com.project.EMRChain.Entities.Auth.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserPrincipalTest
{
    private UserPrincipal userPrincipal = new UserPrincipal();

    @Test
    public void testUserPrincipal()
    {
        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);
        when(user.getName()).thenReturn("Name");
        when(user.getUsername()).thenReturn("Username");
        when(user.getEmail()).thenReturn("Email");
        when(user.getPassword()).thenReturn("Password");

        UserPrincipal result = userPrincipal.create(user);

        assertEquals(result.getId(), user.getId());
        assertEquals(result.getName(), user.getName());
        assertEquals(result.getUsername(), user.getUsername());
        assertEquals(result.getEmail(), user.getEmail());
        assertEquals(result.getPassword(), user.getPassword());
        assertTrue(userPrincipal.isAccountNonExpired());
        assertTrue(userPrincipal.isAccountNonLocked());
        assertTrue(userPrincipal.isEnabled());
        assertTrue(userPrincipal.isAccountNonExpired());
    }
}
