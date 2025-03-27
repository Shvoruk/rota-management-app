package dev.oleksii.rotamanagementapp.reposIT;

import dev.oleksii.rotamanagementapp.domain.entities.Schedule;
import dev.oleksii.rotamanagementapp.domain.entities.Shift;
import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.repos.ShiftRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ShiftRepositoryIT {

    @Autowired
    ShiftRepository shiftRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    void testFindByTeamIdAndShiftId() {

        Team team = new Team();
        team.setName("Team 1");
        team = entityManager.persistAndFlush(team);

        Schedule schedule = new Schedule();
        schedule.setTeam(team);
        schedule = entityManager.persistAndFlush(schedule);

        Shift shift = new Shift();
        shift.setName("Shift");
        shift.setDate(LocalDate.now());
        shift.setStartTime(LocalTime.now());
        shift.setEndTime(LocalTime.now().plusHours(1));
        shift.setSchedule(schedule);
        shift = entityManager.persistAndFlush(shift);

        Optional<Shift> optionalShift = shiftRepository.findByTeamIdAndShiftId(team.getId(), shift.getId());

        assertThat(optionalShift).isPresent();
        assertThat(optionalShift.get().getName()).isEqualTo("Shift");
        assertThat(optionalShift.get().getSchedule()).isEqualTo(schedule);
    }
}