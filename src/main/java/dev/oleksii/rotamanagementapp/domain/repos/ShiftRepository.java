package dev.oleksii.rotamanagementapp.domain.repos;

import dev.oleksii.rotamanagementapp.domain.entities.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, UUID> {
    @Query("SELECT s FROM Shift s WHERE s.id = :shiftId AND s.schedule.team.id = :teamId")
    Optional<Shift> findByTeamIdAndShiftId(@Param("teamId") UUID teamId, @Param("shiftId") UUID shiftId);
}
