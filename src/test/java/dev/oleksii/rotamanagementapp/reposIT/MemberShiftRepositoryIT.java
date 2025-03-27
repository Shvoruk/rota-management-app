package dev.oleksii.rotamanagementapp.reposIT;

import dev.oleksii.rotamanagementapp.domain.entities.MemberShift;
import dev.oleksii.rotamanagementapp.domain.repos.MemberShiftRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class MemberShiftRepositoryIT {

    @Autowired
    MemberShiftRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    void deleteByShiftIdAndId() {
        MemberShift newMemberShift = new MemberShift();
        entityManager.persistAndFlush(newMemberShift);

        assertThat(repository.findById(newMemberShift.getId())).isPresent();

        repository.delete(newMemberShift);
        assertThat(entityManager.find(MemberShift.class, newMemberShift.getId())).isNull();
    }
}