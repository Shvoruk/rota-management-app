package dev.oleksii.rotamanagementapp.domain.repos;

import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.entities.Member;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface MembershipRepository extends JpaRepository<Member, UUID> {
    void deleteByUserAndTeamId(User user, UUID teamId);
    Optional<Member> findByUserAndTeamId(User user, UUID teamId);
    Set<Member> findAllMembershipsByUser(User user);
    boolean existsByUserAndTeam(User user, Team team);
    boolean existsByUserAndTeamId(User user, UUID teamId);
}
