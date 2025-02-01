package dev.oleksii.rotamanagementapp.domain.repos;

import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.entities.TeamMember;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, UUID> {

    void deleteByUserAndTeam(User user, Team team);

    Optional<TeamMember> findByUserAndTeam(User user, Team team);
}
