package dev.oleksii.rotamanagementapp.mappers;

import dev.oleksii.rotamanagementapp.domain.dtos.MemberDTO;
import dev.oleksii.rotamanagementapp.domain.entities.TeamMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TeamMemberMapper {

    @Mappings({
            @Mapping(source = "id", target = "memberId"),
            @Mapping(source = "fullName", target = "memberFullName"),
            @Mapping(source = "role", target = "teamRole")
    })
    MemberDTO toMemberDTO(TeamMember member);

}
