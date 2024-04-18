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
/**
 * Service class responsible for handling JWT (JSON Web Token) related operations.
 *
 * <p>
 * This class provides methods to fetch email from a JWT token, retrieve claims from the token,
 * validate the token against UserDetails, and check if the token is expired.
 * </p>
 *
 * <p>
 * Note: The key used for token verification is defined as a static final field in this class.
 * </p>
 *
 * @version 17 April 2024
 */
@Service
public class JwtService {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    // The secret key used for token verification
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(
            Decoders.BASE64.decode("9216CDCD3ECB8F4F3F09A2F8CC1D0279B2716B73FA9571A6672D1E6ADF1C52F5")
    );

    /**
     * Extracts the email from the JWT token.
     *
     * @param token The JWT token from which the email needs to be extracted.
     * @return The email extracted from the JWT token.
     */
    public String fetchEmail(String token) {
        return fetchClaims(token, Claims::getSubject);
    }

    /**
     * Retrieves claims from the JWT token using the provided claims resolver function.
     *
     * @param <T>             The type of the claim.
     * @param token           The JWT token from which claims need to be retrieved.
     * @param claimsResolver  The function used to extract specific claims from the token.
     * @return The extracted claims.
     */
    public <T> T fetchClaims(String token, Function<Claims, T> claimsResolver) {
        Claims claims = fetchClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Validates whether the provided token is valid for the given UserDetails.
     *
     * @param token        The JWT token to be validated.
     * @param userDetails  The UserDetails object representing the user details.
     * @return True if the token is valid for the provided user details, false otherwise.
     */
    public boolean isValidToken(String token, UserDetails userDetails) {
        return fetchEmail(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Checks whether the provided token is expired.
     *
     * @param token The JWT token to be checked for expiration.
     * @return True if the token is expired, false otherwise.
     */
    public boolean isTokenExpired(String token) {
        return fetchClaims(token, Claims::getExpiration)
                .before(Timestamp.valueOf(LocalDateTime.now()));
    }


    public String generateToken(Map<String, Objects> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(Timestamp.valueOf(LocalDateTime.now().plusMinutes(180)))
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
