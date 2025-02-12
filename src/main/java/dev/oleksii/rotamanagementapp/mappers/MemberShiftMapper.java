package dev.oleksii.rotamanagementapp.mappers;

import dev.oleksii.rotamanagementapp.domain.dtos.MemberShiftDTO;
import dev.oleksii.rotamanagementapp.domain.entities.MemberShift;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TeamMemberMapper.class})
public interface MemberShiftMapper {

    @Mapping(source = "id", target = "memberShiftId")
    MemberShiftDTO toMemberShiftDTO(MemberShift memberShift);
}