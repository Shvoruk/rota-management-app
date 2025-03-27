package dev.oleksii.rotamanagementapp.domain.repos;

import dev.oleksii.rotamanagementapp.domain.entities.MemberShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MemberShiftRepository extends JpaRepository<MemberShift, UUID> {
    void deleteByShiftIdAndId(UUID shiftId, UUID id);
}
