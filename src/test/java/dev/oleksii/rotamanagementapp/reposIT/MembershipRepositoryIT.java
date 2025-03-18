package dev.oleksii.rotamanagementapp.reposIT;

import dev.oleksii.rotamanagementapp.domain.entities.Member;
import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.enums.Role;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;
import dev.oleksii.rotamanagementapp.domain.repos.MembershipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class MembershipRepositoryIT {

    @Autowired
    MembershipRepository membershipRepository;

    @Autowired
    TestEntityManager entityManager;

    User user;
    Team team;
    Member member;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .fullName("Test")
                .password("password")
                .email("test@email.com")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .verified(true)
                .build();
        entityManager.persist(user);

        team = new Team();
        team.setName("Test Team");
        entityManager.persistAndFlush(team);

        member = Member.builder()
                .fullName("Member")
                .role(TeamRole.EMPLOYEE)
                .team(team)
                .user(user)
                .build();
        entityManager.persistAndFlush(member);
    }

    @Test
    void testExistsByUserIdAndTeamId() {
        boolean exists = membershipRepository.existsByUserIdAndTeamId(user.getId(), team.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByUserIdAndTeamIdAndRole() {
        boolean exists = membershipRepository.existsByUserIdAndTeamIdAndRole(user.getId(), team.getId(), TeamRole.EMPLOYEE);
        assertThat(exists).isTrue();
    }

    @Test
    void testFindByUserIdAndTeamId() {
        Optional<Member> foundMember = membershipRepository.findByUserIdAndTeamId(user.getId(), team.getId());

        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getId()).isEqualTo(member.getId());
        assertThat(foundMember.get().getUser()).isEqualTo(user);
        assertThat(foundMember.get().getTeam()).isEqualTo(team);
    }

    @Test
    void testFindAllByTeamId() {
        Set<Member> members = membershipRepository.findAllByTeamId(team.getId());

        assertThat(members.size()).isEqualTo(1);
        assertThat(members.iterator().next()).isEqualTo(member);
    }

    @Test
    void testDeleteByUserIdAndTeamId() {
        membershipRepository.deleteByUserIdAndTeamId(user.getId(), team.getId());
        entityManager.flush();

        boolean exists = membershipRepository.existsByUserIdAndTeamId(user.getId(), team.getId());

        assertThat(exists).isFalse();
    }
}