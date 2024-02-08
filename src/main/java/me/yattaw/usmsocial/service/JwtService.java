package me.yattaw.usmsocial.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(
            Decoders.BASE64.decode("9216CDCD3ECB8F4F3F09A2F8CC1D0279B2716B73FA9571A6672D1E6ADF1C52F5")
    );

    public String fetchEmail(String token) {
        return fetchClaims(token, Claims::getSubject);
    }

    public <T> T fetchClaims(String token, Function<Claims, T> claimsResolver) {
        Claims claims = fetchClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        return fetchEmail(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return fetchClaims(token, Claims::getExpiration)
                .before(Timestamp.valueOf(LocalDateTime.now()));
    }

    public String generateToken(Map<String, Objects> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusMinutes(1)))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public Claims fetchClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractToken(HttpServletRequest request) {
        String authorization = request.getHeader(AUTH_HEADER);
        if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
            return authorization.substring(BEARER_PREFIX.length());
        }
        return null;
    }

}
