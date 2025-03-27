package dev.oleksii.rotamanagementapp.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.oleksii.rotamanagementapp.domain.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Represents an application user.
 * Implements UserDetails to integrate with Spring Security.
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// Excludes associations like memberships and verificationToken to avoid circular references and performance issues during equality checks.
@EqualsAndHashCode(exclude = {"memberships", "verificationToken"})
// Prevents serialization issues with Hibernate proxies.
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Indicates whether the user's email has been verified.
    @Column(nullable = false)
    private boolean verified;

    // The set of memberships linking the user to various teams.
    // Cascade type REMOVE ensures that deleting a user removes associated memberships.
    // FetchType.LAZY defers loading until needed.
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private Set<Member> memberships = new HashSet<>();

    // Embedded verification token for account confirmation.
    @Embedded
    private VerificationToken verificationToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Indicates whether the user is enabled (verified).
    @Override
    public boolean isEnabled() {
        return this.verified;
    }

}
