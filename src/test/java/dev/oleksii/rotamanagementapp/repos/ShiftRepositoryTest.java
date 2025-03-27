package dev.oleksii.rotamanagementapp.repos;

import dev.oleksii.rotamanagementapp.domain.entities.Schedule;
import dev.oleksii.rotamanagementapp.domain.entities.Shift;
import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.repos.ScheduleRepository;
import dev.oleksii.rotamanagementapp.domain.repos.ShiftRepository;
import dev.oleksii.rotamanagementapp.domain.repos.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@DataJpaTest
public class ShiftRepositoryTest {

    @Autowired
    ShiftRepository shiftRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    void testFindByTeamIdAndShiftId() {

        Team team = Team.builder()
                .name("Team 1")
                .build();
        teamRepository.save(team);

        Schedule schedule = Schedule.builder()
                .team(team)
                .build();
        scheduleRepository.save(schedule);

        Shift shift = Shift.builder()
                .name("Shift")
                .date(LocalDate.now())
                .startTime(LocalTime.now())
                .endTime(LocalTime.now().plusHours(1))
                .schedule(schedule)
                .build();

        shiftRepository.save(shift);

        Optional<Shift> optionalShift = shiftRepository.findByTeamIdAndShiftId(team.getId(), shift.getId());
        assert optionalShift.isPresent();
        assert optionalShift.get().getName().equals("Shift");
        assert optionalShift.get().getSchedule().equals(schedule);
    }
}
