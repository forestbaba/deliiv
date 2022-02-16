package com.deliiv.server.security;


import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtTokenProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtsecret}")
    private String jwtsecret;

    @Value("${app.expiredTime}")
    private int expiredTime;

    public String generateToken(Authentication authentication){
       // UserPrincipalParent userPrincipalParent = (UserPrincipalParent) authentication.getPrincipal();
        User user = (User) authentication.getPrincipal();
        Date date = new Date();

        Date expiryDate = new Date(date.getTime() + expiredTime);

        return Jwts.builder().setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512,jwtsecret)
                .compact();
    }

    public String getUserIdFromJwt(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtsecret)
                .parseClaimsJws(token)
                .getBody();

        return  claims.getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtsecret).parseClaimsJws(token);
            return true;
        }catch (SignatureException e){
            LOGGER.error("Invalid Jwt signature");
        }catch (MalformedJwtException e){
            LOGGER.error("Invalid jwt token");
        }catch (ExpiredJwtException e){
            LOGGER.error("Expired Jwt token");
        }catch (UnsupportedJwtException e){
            LOGGER.error("Unsupported jwt token");
        }catch (IllegalArgumentException e){
            LOGGER.error("Jwt claims string is empty");
        }
        return false;
    }


}
