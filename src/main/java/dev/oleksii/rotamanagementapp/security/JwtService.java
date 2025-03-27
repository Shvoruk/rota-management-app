package dev.oleksii.rotamanagementapp.security;

import dev.oleksii.rotamanagementapp.configuration.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig jwtConfig;

    /**
     * Extracts the username (subject) from the provided JWT token.
     *
     * @param token the JWT token
     * @return the username (subject) contained in the token
     */
    String getUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     * Generic method to extract a claim from the JWT token.
     *
     * @param token           the JWT token
     * @param claimsResolver  a function to extract a specific claim from the Claims object
     * @param <T>             the type of the claim
     * @return the extracted claim
     */
    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        // Get all claims from the token.
        final Claims claims = getAllClaims(token);
        // Apply the provided function to extract the desired claim.
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT token for the given user without any extra claims.
     *
     * @param userDetails the details of the user for whom the token is generated
     * @return a signed JWT token as a String
     */
    public String generateToken(UserDetails userDetails) {
        // Create an empty map for extra claims.
        Map<String, Object> claims = new HashMap<>();
        // Generate the token using the empty extra claims.
        return generateToken(claims, userDetails);
    }

    /**
     * Generates a JWT token for the given user with extra claims.
     *
     * @param extraClaims additional claims to be included in the token
     * @param userDetails the details of the user for whom the token is generated
     * @return a signed JWT token as a String
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims) // Set any additional claims.
                .setSubject(userDetails.getUsername()) // Set the subject (username).
                .setIssuedAt(new Date(System.currentTimeMillis())) // Mark the token issuance time.
                // Set the expiration time by adding the configured duration to the current time.
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationMilliseconds()))
                // Sign the token with the signing key using the HS256 algorithm.
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact(); // Build the JWT and serialize it as a compact, URL-safe string.
    }

    /**
     * Validates whether the provided token is valid for the given user.
     *
     * @param token       the JWT token to validate
     * @param userDetails the user's details to compare against the token's subject
     * @return true if the token is valid, false otherwise
     */
    boolean isTokenValid(String token, UserDetails userDetails) {
        // Extract the username from the token.
        final String username = getUsername(token);
        // Token is valid if the username matches and the token hasn't expired.
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if the token has expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    /**
     * Retrieves the expiration date from the token.
     *
     * @param token the JWT token
     * @return the expiration date of the token
     */
    private Date getExpiration(String token) {
        // Extract the expiration claim from the token.
        return getClaim(token, Claims::getExpiration);
    }

    /**
     * Parses the JWT token to extract all its claims.
     *
     * @param token the JWT token
     * @return the Claims object containing all token data
     */
    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder() // Create a JWT parser builder.
                .setSigningKey(getSigningKey()) // Set the key to validate the token's signature.
                .build() // Build the parser.
                .parseClaimsJws(token) // Parse the token.
                .getBody(); // Retrieve the body of the token (claims).
    }

    /**
     * Retrieves the signing key from the Base64-encoded secret stored in the JwtConfig.
     *
     * @return the signing Key
     */
    private Key getSigningKey() {
        // Decode the Base64-encoded secret key.
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecret());
        // Create and return an HMAC-SHA key using decoded bytes.
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
