package dev.oleksii.rotamanagementapp.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a member of a team.
 * Links a user to a team with a specific team role.
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// Excludes associations to avoid circular references and performance issues during equality checks.
@EqualsAndHashCode(exclude = {"user", "team", "memberShifts"})
// Prevents serialization issues with Hibernate proxies.
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "members", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "team_id"})
})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    // Role of the member within the team (e.g., EMPLOYEE, MANAGER).
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TeamRole role;

    // The user associated with this membership.
    // This association is required and lazily loaded.
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    // The team to which this member belongs.
    // Annotated with @JsonBackReference to prevent recursion when serializing to JSON.
    @JsonBackReference
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Team team;

    // Collection of shifts assigned to the member.
    @JsonManagedReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MemberShift> memberShifts = new HashSet<>();

}
