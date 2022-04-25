package com.db.javaunittests.service;

import com.db.javaunittests.exception.JWTException;
import com.db.javaunittests.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    @Value("${com.db.javaunittests.secret}")
    private String secret;      // secret key used in HMAC
    @Value("${com.db.javaunittests.jwtTimeout}")
    private Long jwtTimeout;    // in milliseconds

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Creates a token with the username and empty claims.
     *
     * @param userDetails
     * @return new token as String
     */
    public String createTokenFromUserDetails(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createTokenFromClaimsAndSubject(claims, userDetails.getUsername());
    }

    /**
     * Creates a token with given claims and subject.
     *
     * @param claims
     * @param subject
     * @return new token as String
     */
    private String createTokenFromClaimsAndSubject(Map<String, Object> claims, String subject) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtTimeout)).signWith(key).compact();
    }

    /**
     * Validates the token associated with the user details.
     *
     * @param token to validate
     * @param user
     * @return true if valid
     * @throws JWTException in case of invalid token
     */
    public Boolean validateToken(String token, User user) throws JWTException {
        final String email = extractEmail(token);

        if (!email.equals((user.getUsername()))) {
            throw new JWTException(email + " - user token is invalid.");
        }
        if (isTokenExpired(token)) {
            throw new JWTException(email + " - user token is expired.");
        }
        if (!user.isEnabled()) {
            throw new JWTException(email + " - user is disabled.");
        }
        if (!user.isAccountNonExpired()) {
            throw new JWTException(email + " - user account is expired.");
        }
        if (!user.isCredentialsNonExpired()) {
            throw new JWTException(email + " - user credentials are expired.");
        }
        if (!user.isAccountNonLocked()) {
            throw new JWTException(email + " - user the account is locked.");
        }

        return true;
    }
}
