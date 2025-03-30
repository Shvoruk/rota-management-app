package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.entities.MemberShift;
import dev.oleksii.rotamanagementapp.domain.repos.MemberShiftRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberShiftServiceImplTest {

    @Mock
    private MemberShiftRepository repository;

    @InjectMocks
    private MemberShiftServiceImpl service;

    @Test
    void saveMemberShift() {
        MemberShift memberShift = new MemberShift();
        service.saveMemberShift(memberShift);
        verify(repository).save(memberShift);
    }

    @Test
    void deleteMemberShiftByShiftIdAndId() {
        UUID shiftId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        service.deleteMemberShiftByShiftIdAndId(shiftId, teamId);
        verify(repository).deleteByShiftIdAndId(shiftId, teamId);
    }
}