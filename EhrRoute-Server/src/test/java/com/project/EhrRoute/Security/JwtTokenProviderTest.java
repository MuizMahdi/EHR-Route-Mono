package com.project.EMRChain.Security;
import com.project.EhrRoute.Security.JwtTokenProvider;
import com.project.EhrRoute.Security.UserPrincipal;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JwtTokenProviderTest
{
    private JwtTokenProvider tokenProvider = new JwtTokenProvider();

    private String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0" +
    "IjoxNTQxNDQ5NzE5LCJleHAiOjE1NDE0NDk3MTl9.BgS1Rv_v-U7x2i3or_ESEc" +
    "VSPIpIy60Fb41bcfwfTFY8fFxHDwBpsseU-l8LlAcSqI06roNfHZH2G3P8oPRHoA";

    @Before
    public void initToken()
    {
        tokenProvider.jwtSecret = "jwtSecret";
    }

    @Test
    public void testGenerateJwt()
    {
        Authentication authentication = mock(Authentication.class);
        UserPrincipal userPrincipal = mock(UserPrincipal.class);

        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(userPrincipal.getId()).thenReturn(1L);

        String jwt = tokenProvider.generateJWT(authentication);

        assertNotNull(jwt);
        assertTrue(tokenProvider.generateJWT(authentication) instanceof String);
        assertFalse(jwt.isEmpty());
    }

    @Test(expected = ExpiredJwtException.class)
    public void testGetUserIdFromJwt()
    {
        assertNotNull(tokenProvider.getUserIdFromJwt(token));
        assertTrue(tokenProvider.getUserIdFromJwt(token) instanceof Long);
    }

    @Test(expected = ExpiredJwtException.class)
    public void testValidateToken()
    {
        assertTrue(tokenProvider.validateToken(token));
    }

}
