package dev.oleksii.rotamanagementapp.mappers;

import dev.oleksii.rotamanagementapp.domain.dtos.MemberShiftDto;
import dev.oleksii.rotamanagementapp.domain.entities.MemberShift;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MemberMapper.class})
public interface MemberShiftMapper {

    @Mapping(source = "id", target = "memberShiftId")
    MemberShiftDto toMemberShiftDTO(MemberShift memberShift);

}