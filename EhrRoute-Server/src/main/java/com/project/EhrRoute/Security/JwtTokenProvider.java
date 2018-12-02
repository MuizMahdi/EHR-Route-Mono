package com.project.EhrRoute.Security;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;


/*
*   Used for generating a JWT after a user logs in successfully,
*   and validating the JWT sent in the Authorization header of the requests.
*/


@Component
public class JwtTokenProvider
{
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    public String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;


    public String generateJWT(Authentication authentication)
    {
        // Get current authenticated user
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // Get current time
        Long currentTime = new Date().getTime();

        // Create the token's expiration date
        Date expiryDate = new Date(currentTime + jwtExpirationInMs);

        // Return the JWT
        return Jwts.builder()
            .setSubject(Long.toString(userPrincipal.getId())) // Authenticated User ID as Subject
            .setIssuedAt(new Date()) // Time at which the JWT is created and sent to client
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, jwtSecret) // JWT Signature
        .compact();
    }

    public Long getUserIdFromJwt(String token)
    {
        // Get the JWT Payload Claims, which contains the user info
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

        // Return Subject, which has the user ID
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String token)
    {
        try
        {
            // If no exception is thrown, then the JWT is valid
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }
        catch (SignatureException e) {
            logger.error("Invalid JWT Signature");
        }
        catch (MalformedJwtException e) {
            logger.error("Invalid JWT Token");
        }
        catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT Token");
        }
        catch (IllegalArgumentException e) {
            logger.error("JWT Claims string is empty.");
        }

        return false;
    }

}
