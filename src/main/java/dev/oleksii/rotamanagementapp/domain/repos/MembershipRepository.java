package dev.oleksii.rotamanagementapp.domain.repos;

import dev.oleksii.rotamanagementapp.domain.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MembershipRepository extends JpaRepository<Member, UUID> {
}
