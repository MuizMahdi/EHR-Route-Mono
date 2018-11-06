package com.project.EMRChain.Security;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthenticationFilterTest
{
    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AppUserDetailsService userDetailsService;

    @InjectMocks
    private JwtAuthenticationFilter authenticationFilter;


    @Test
    public void testDoFilterInternal() throws ServletException, IOException
    {
        String authHeader = "Bearer [TOKEN]";
        String jwt = "[TOKEN]";

        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("Authorization")).thenReturn(authHeader);

        when(tokenProvider.validateToken(jwt)).thenReturn(true);

        when(tokenProvider.getUserIdFromJwt(jwt)).thenReturn(1L);

        UserDetails userDetails = mock(UserDetails.class);

        when(userDetailsService.loadUserById(1L)).thenReturn(userDetails);

        when(userDetails.getAuthorities()).thenReturn(Mockito.any());

        authenticationFilter.doFilterInternal(request, response, filterChain);


        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void testGetJwtFromRequest()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer [TOKEN]");

        String token = authenticationFilter.getJwtFromRequest(request);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals(token, "[TOKEN]");
    }

    @Test
    public void testGetJwtFromRequestReturnsNull()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("Authorization")).thenReturn("FakeToken");

        String token = authenticationFilter.getJwtFromRequest(request);

        assertNull(token);
    }
}
