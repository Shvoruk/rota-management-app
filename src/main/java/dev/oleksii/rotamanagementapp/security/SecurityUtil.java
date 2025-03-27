package dev.oleksii.rotamanagementapp.security;

import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;
import dev.oleksii.rotamanagementapp.domain.repos.MembershipRepository;
import dev.oleksii.rotamanagementapp.domain.repos.UserRepository;
import dev.oleksii.rotamanagementapp.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;

    public User getCurrentUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException("User not found."));
    }

    public boolean isMember(UUID userId, UUID teamId) {
        return membershipRepository.existsByUserIdAndTeamId(userId, teamId);
    }

    public boolean isManager(UUID userId, UUID teamId) {
        return membershipRepository.existsByUserIdAndTeamIdAndRole(userId, teamId, TeamRole.MANAGER);
    }

}
