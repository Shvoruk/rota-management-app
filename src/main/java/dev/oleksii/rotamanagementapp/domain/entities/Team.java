package dev.oleksii.rotamanagementapp.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a team within the application.
 * A team has a collection of members and an associated schedule.
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// Excludes collections to avoid recursive calls.
@EqualsAndHashCode(exclude = {"members", "schedule"})
// Ignores Hibernate internals when serializing to JSON.
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    // Used in bidirectional relationships to handle serialization,
    // preventing infinite recursion with the corresponding @JsonBackReference in the Member entity.
    @JsonManagedReference
    // The set of members that belong to this team.
    // CascadeType.ALL and orphanRemoval=true mean that any changes (persist, remove) propagate,
    // and members removed from the set are automatically deleted from the database.
    // FetchType.LAZY avoids unnecessary loading.
    @Builder.Default  // Ensures initialization when using the builder
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Member> members = new HashSet<>();


    @JsonManagedReference
    // The schedule associated with the team.
    // One-to-one relationship with Schedule.
    // Cascade and lazy fetching apply here as well.
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Schedule schedule;

    public void addMember(Member member) {
        members.add(member);
        member.setTeam(this);
    }
}
