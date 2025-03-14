package com.example.booking.config.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Getter
@Service
public class  JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value(value = "${spring.security.jwt.expiration-time}")
    private long expirationTime;

    @Value(value = "${spring.security.jwt.key}")
    private String keyJWT;

    @Value(value = "${spring.security.jwt.issuer}")
    private String issuer;

    private SecretKey key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(keyJWT));
    }
    public String extractUsername(String token) throws GeneralSecurityException, IOException {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws GeneralSecurityException, IOException {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) throws GeneralSecurityException, IOException {
        return Jwts.parser()
                .verifyWith(key())
                .build().parseSignedClaims(token).getPayload();
    }

    public String generateToken(UserDetails userDetails) throws GeneralSecurityException, IOException {
        if(!userDetails.isEnabled()) throw new BadCredentialsException("The account has been banned", new Throwable("username")) ;
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) throws GeneralSecurityException, IOException {

        return buildToken( userDetails, expirationTime);
    }
    private String buildToken(
            UserDetails userDetails,
            long expiration) {
        return Jwts
                .builder()
                .signWith(key())
                .subject(userDetails.getUsername())
                .claim("role", userDetails.getAuthorities().toArray()[0].toString())
                .issuer(issuer)
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) throws GeneralSecurityException, IOException {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && userDetails.isEnabled()) ;
    }

    private Date extractExpiration(String token) throws GeneralSecurityException, IOException {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) throws GeneralSecurityException, IOException {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateJwtToken(String token){
        try
        {
            Jwts.parser()
                    .verifyWith(key())
                    .build().parseSignedClaims(token).getPayload();
            return true;
        }
        catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            logger.error( e.getMessage());
            return false;
        }


    }

}
