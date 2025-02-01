package dev.oleksii.rotamanagementapp.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_shifts", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "shift_id", "date"})
})
public class UserShift {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Many-to-One: UserShift ↔ User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Many-to-One: UserShift ↔ Shift
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shift;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private LocalDate date; // The date of the shift assignment

    @Column(nullable = false)
    private boolean isAvailable; // User's availability for the shift

}

