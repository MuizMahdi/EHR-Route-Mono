package com.project.EMRChain.Security;
import com.project.EhrRoute.Security.JwtAuthenticationEntryPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthenticationEntryPointTest
{
    private JwtAuthenticationEntryPoint entryPoint = new JwtAuthenticationEntryPoint();

    @Test
    public void testCommence() throws IOException, ServletException
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException Ex = mock(AuthenticationException.class);

        when(Ex.getMessage()).thenReturn("ExceptionMessage");

        entryPoint.commence(request, response, Ex);

        verify(response, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED, Ex.getMessage());
    }
}
