package dev.oleksii.rotamanagementapp.repos;

import dev.oleksii.rotamanagementapp.domain.entities.Schedule;
import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.repos.ScheduleRepository;
import dev.oleksii.rotamanagementapp.domain.repos.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ScheduleRepositoryTest {

    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    void testFindByTeamId() {
        Team team = Team.builder()
                .name("Team")
                .build();
        teamRepository.save(team);

        Schedule schedule = Schedule.builder()
                .team(team)
                .build();
        scheduleRepository.save(schedule);

        Optional<Schedule> scheduleOptional = scheduleRepository.findByTeamId(team.getId());
        assert scheduleOptional.isPresent();
        assert scheduleOptional.get().getTeam().equals(team);
    }
}
