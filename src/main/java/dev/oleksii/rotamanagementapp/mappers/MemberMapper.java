package dev.oleksii.rotamanagementapp.mappers;

import dev.oleksii.rotamanagementapp.domain.dtos.MemberDto;
import dev.oleksii.rotamanagementapp.domain.entities.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(source = "id", target = "memberId")
    MemberDto toMemberDTO(Member member);

}
