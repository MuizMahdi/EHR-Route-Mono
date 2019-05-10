package com.project.EhrRoute.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/*
*   This class is used to return a '401 unauthorized error' to clients that try to access a protected
*   resource without proper authentication. It implements Spring Security’s AuthenticationEntryPoint
*   interface.
*
*   The commence() method is called whenever an exception is thrown due to an unauthenticated user
*   trying to access a resource that requires authentication.
*
*   Mostly called when a user tries to authenticate with an expired JWT.
*/


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint
{
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException Ex) throws IOException, ServletException
    {
        logger.error("Responding with unauthorized error. Message - {}", Ex.getMessage());
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, Ex.getMessage());
    }
}
