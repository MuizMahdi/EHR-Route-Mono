package com.project.EMRChain.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/*
*   Gets the JWT from the request, validates it, loads the user associated with the token,
*   and passes it to Spring Security Context Holder.
*/


public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private JwtTokenProvider tokenProvider;
    private AppUserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, AppUserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }
    public JwtAuthenticationFilter() {}



    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException
    {
        try
        {
            String jwt = getJwtFromRequest(httpServletRequest);

            // If text, valid token
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {

                Long userId = tokenProvider.getUserIdFromJwt(jwt);

                // Get user details from ID
                UserDetails userDetails = userDetailsService.loadUserById(userId);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        // Create an authentication token using the user details
                        userDetails, null, userDetails.getAuthorities()
                );

                // Set the token details from the request
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                // Set the authentication in the Security Context Holder
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        catch (Exception Ex)
        {
            logger.error("Could not set user authentication in security context", Ex);
        }

        // Pass the request and response to the next filter on the chain
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtFromRequest(HttpServletRequest request)
    {
        // Get token string from the 'Authorization' header
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
    }
}
