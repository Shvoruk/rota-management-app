package dev.oleksii.rotamanagementapp.domain.repos;

import dev.oleksii.rotamanagementapp.domain.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    @Query("SELECT s.team.id FROM Schedule s WHERE s.id = :scheduleId")
    UUID findTeamIdByScheduleId(@Param("scheduleId") UUID scheduleId);
    Optional<Schedule> findByTeamId(UUID teamId);
}
