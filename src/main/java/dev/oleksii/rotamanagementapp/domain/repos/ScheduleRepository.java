package dev.oleksii.rotamanagementapp.domain.repos;

import dev.oleksii.rotamanagementapp.domain.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    Optional<Schedule> findByTeamId(UUID teamId);
}
