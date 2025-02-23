package dev.oleksii.rotamanagementapp.domain.repos;

import dev.oleksii.rotamanagementapp.domain.entities.Member;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface MembershipRepository extends JpaRepository<Member, UUID> {
    boolean existsByUserIdAndTeamId(UUID userId, UUID teamId);
    boolean existsByUserIdAndTeamIdAndRole(UUID userId, UUID teamId, TeamRole role);
    Optional<Member> findByUserIdAndTeamId(UUID userId, UUID teamId);
    void deleteByUserIdAndTeamId(UUID userId, UUID teamId);
    Set<Member> findAllByTeamId(UUID teamId);
}
