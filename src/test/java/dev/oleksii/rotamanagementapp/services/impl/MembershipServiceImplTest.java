package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.entities.Member;
import dev.oleksii.rotamanagementapp.domain.repos.MembershipRepository;
import dev.oleksii.rotamanagementapp.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MembershipServiceImplTest {

    @Mock
    private MembershipRepository membershipRepository;

    @InjectMocks
    private MembershipServiceImpl membershipService;

    @Test
    void getMembershipById() {
        UUID memberId = UUID.randomUUID();
        Member member = new Member();
        member.setId(memberId);

        when(membershipRepository.findById(memberId))
                .thenReturn(Optional.of(member));

        var result = membershipService.getMembershipById(memberId);

        verify(membershipRepository).findById(memberId);
        assertNotNull(result);
        assertEquals(member, result);
        assertEquals(result.getId(), memberId);
    }

    @Test
    void getMembershipByIdThrows() {
        UUID memberId = UUID.randomUUID();

        when(membershipRepository.findById(memberId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> membershipService.getMembershipById(memberId));
        verify(membershipRepository).findById(memberId);

    }

    @Test
    void getMembershipByUserIdAndTeamId() {
        UUID userId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        Member member = new Member();

        when(membershipRepository.findByUserIdAndTeamId(userId, teamId))
                .thenReturn(Optional.of(member));

        var result = membershipService.getMembershipByUserIdAndTeamId(userId, teamId);

        verify(membershipRepository).findByUserIdAndTeamId(userId, teamId);
        assertNotNull(result);
        assertEquals(member, result);
        assertEquals(result.getId(), member.getId());
    }

    @Test
    void getMembershipByUserIdAndTeamIdThrows() {
        UUID userId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();

        when(membershipRepository.findByUserIdAndTeamId(userId, teamId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> membershipService.getMembershipByUserIdAndTeamId(userId, teamId));
        verify(membershipRepository).findByUserIdAndTeamId(userId, teamId);
    }

    @Test
    void getAllMembershipsByTeamId() {
        UUID teamId = UUID.randomUUID();
        Member member = new Member();

        when(membershipRepository.findAllByTeamId(teamId))
                .thenReturn(Set.of(member));

        var result = membershipService.getAllMembershipsByTeamId(teamId);

        verify(membershipRepository).findAllByTeamId(teamId);
        assertNotNull(result);
        assertEquals(Set.of(member), result);

    }

    @Test
    void deleteMembershipByUserIdAndTeamId() {
        UUID userId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        membershipService.deleteMembershipByUserIdAndTeamId(userId, teamId);
        verify(membershipRepository).deleteByUserIdAndTeamId(userId, teamId);
    }
}