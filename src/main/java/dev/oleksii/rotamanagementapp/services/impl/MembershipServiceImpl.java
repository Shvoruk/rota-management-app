package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.entities.Member;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;
import dev.oleksii.rotamanagementapp.security.SecurityUtil;
import dev.oleksii.rotamanagementapp.services.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final SecurityUtil securityUtil;

    @Override
    public Member getMembership(Principal principal, UUID teamId) {
        var user = securityUtil.getCurrentUser(principal);

        return user.getMemberships().stream()
                .filter(m -> m.getTeam().getId().equals(teamId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User is not a member of this team"));
    }

    public void checkMembership(Principal principal, UUID teamId) {
        var user = securityUtil.getCurrentUser(principal);
        user.getMemberships().stream()
                .filter(m -> m.getTeam().getId().equals(teamId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("User is not a member of this team"));
    }

    @Override
    public User checkNoMembership(Principal principal, UUID teamId) {
        var user = securityUtil.getCurrentUser(principal);
        if(user.getMemberships().stream().anyMatch(m -> m.getTeam().getId().equals(teamId))){
            throw new IllegalArgumentException("User is already a member of this team");
        }
        return user;
    }

    public void checkManagerMembership(Principal principal, UUID teamId) {
        var user = securityUtil.getCurrentUser(principal);
        user.getMemberships().stream()
                .filter(m -> m.getRole() == TeamRole.MANAGER && m.getTeam().getId().equals(teamId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("User is not a manager of this team"));
    }
}
