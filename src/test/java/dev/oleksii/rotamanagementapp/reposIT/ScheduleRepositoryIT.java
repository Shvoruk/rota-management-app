package dev.oleksii.rotamanagementapp.reposIT;

import dev.oleksii.rotamanagementapp.domain.entities.Schedule;
import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.repos.ScheduleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ScheduleRepositoryIT {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    void testFindByTeamId() {

        Team team = new Team();
        team.setName("Team");
        entityManager.persistAndFlush(team);

        Schedule schedule = new Schedule();
        schedule.setTeam(team);
        entityManager.persistAndFlush(schedule);

        Optional<Schedule> scheduleOptional = scheduleRepository.findByTeamId(team.getId());

        assertThat(scheduleOptional).isPresent();
        assertThat(scheduleOptional.get().getTeam()).isEqualTo(team);
    }
}