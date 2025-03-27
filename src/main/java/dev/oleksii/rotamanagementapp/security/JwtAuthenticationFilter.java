package dev.oleksii.rotamanagementapp.security;

import dev.oleksii.rotamanagementapp.services.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter is a custom filter that intercepts incoming HTTP requests
 * to validate JWT tokens and authenticate users based on the token's validity.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Filters each incoming request to check for a valid JWT token in the Authorization header.
     *
     * @param request     The incoming HTTP request
     * @param response    The HTTP response
     * @param filterChain The filter chain to pass the request/response to the next filter
     * @throws ServletException If an error occurs during filtering
     * @throws IOException      If an input or output error occurs
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Retrieve the Authorization header from the HTTP request
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;

        // Check if the Authorization header is present and starts with "Bearer "
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            // If not, continue the filter chain without setting authentication
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT token by removing the "Bearer " prefix
        jwt = authorizationHeader.substring(7);

        // Extract the username (email) from the JWT token
        email = jwtService.getUsername(jwt);

        // Proceed only if the email is present and the user is not already authenticated
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details using the extracted email
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

            // Validate the JWT token against the user details
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Create an authentication token with user details and authorities
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Set additional details from the request to the authentication token
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Set the authentication token in the SecurityContext to mark the user as authenticated
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Continue the filter chain after processing
        filterChain.doFilter(request, response);
    }
}
