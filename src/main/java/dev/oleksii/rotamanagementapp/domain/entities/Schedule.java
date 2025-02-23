package dev.oleksii.rotamanagementapp.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a schedule associated with a team.
 * Contains a set of shifts.
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// Excludes collections to avoid recursive calls.
@EqualsAndHashCode(exclude = {"team", "shifts"})
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // The team that owns this schedule.
    // @JsonBackReference avoids recursive serialization with the forward reference in Team.
    @JsonBackReference
    @JoinColumn
    @OneToOne(fetch = FetchType.LAZY)
    private Team team;

    // Collection of shifts in this schedule.
    // @JsonManagedReference marks this side as the one to be serialized.
    @JsonManagedReference
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Shift> shifts = new HashSet<>();

    public void addShift(Shift shift) {
        shifts.add(shift);
        shift.setSchedule(this);
    }
}

