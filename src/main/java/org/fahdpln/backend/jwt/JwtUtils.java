package org.fahdpln.backend.jwt;

import org.fahdpln.backend.user.User;
import org.fahdpln.backend.user.UserDTO;
import org.fahdpln.backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import java.util.Map;

@Component
public class JwtUtils {
    private static final String SECRET_KEY = "QAR1At+Vv3rNXrmiHGcZ0tG3+EPgdkeVi/HqjgcUbf8WcgdgpCXDTjb7CnIv9WEL";

    @Autowired
    private UserRepository userRepository;

    // Find the user form the Bearer token
    public User extractUserFromBeaererToken(String bearerToken) {
        String token = bearerToken.substring(7);
        String username = extractUsername(token);
        User user = userRepository.findByUsername(username).orElse(null);
        return user;
    }

    // Extract username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract role from token
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // Extract id from token
    public Long extractId(String token) {
        return extractClaim(token, claims -> claims.get("id", Long.class));
    }

    // Create token with extra claims
    public String createToken(UserDTO userDto) {

        Map<String, Object> extracClaims = new HashMap<>();
        extracClaims.put("role", userDto.getRole());
        return Jwts
                .builder()
                .setClaims(extracClaims)
                .setSubject(userDto.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    // Validate token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract claim from token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims cliams = extractAllClaims(token);
        return claimsResolver.apply(cliams);
    }

    // Extract all claims from token
    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Get signing key
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
