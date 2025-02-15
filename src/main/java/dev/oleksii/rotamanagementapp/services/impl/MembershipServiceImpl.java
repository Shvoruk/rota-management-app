package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.entities.Member;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.repos.MembershipRepository;
import dev.oleksii.rotamanagementapp.services.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;

    @Transactional(readOnly = true)
    @Override
    public Set<Member> getAllMemberships(User user) {
        return membershipRepository.findAllMembershipsByUser(user);
    }

}
