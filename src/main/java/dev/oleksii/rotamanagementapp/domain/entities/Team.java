package dev.oleksii.rotamanagementapp.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "team", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<TeamMember> members = new HashSet<>();

    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL, optional = false)
    private Schedule schedule;

    public void addMember(TeamMember member) {
        members.add(member);
//        member.setTeam(this);
    }

    public void removeMember(TeamMember member) {
        members.remove(member);
//        member.setTeam(null);
    }


}
