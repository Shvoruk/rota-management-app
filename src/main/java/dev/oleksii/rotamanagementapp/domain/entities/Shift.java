package dev.oleksii.rotamanagementapp.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a work shift within a schedule.
 * Contains details about the shift timing and assigned members.
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// Excludes collections to avoid recursive calls.
@EqualsAndHashCode(exclude = {"schedule", "memberShifts"})
@Table(name = "shifts")
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    // The schedule to which this shift belongs.
    // @JsonBackReference prevents serializing the schedule again within a shift.
    @JsonBackReference
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Schedule schedule;

    // Collection of member shifts (assignments) for this shift.
    // Cascade and orphanRemoval ensure that any changes to assignments are persisted/removed accordingly.
    @JsonManagedReference
    @OneToMany(mappedBy = "shift" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MemberShift> memberShifts = new HashSet<>();

    public void addMemberShift(MemberShift memberShift) {
        memberShifts.add(memberShift);
        memberShift.setShift(this);
    }
}

