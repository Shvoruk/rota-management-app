package dev.oleksii.rotamanagementapp.mappers;

import dev.oleksii.rotamanagementapp.domain.dtos.ShiftDto;
import dev.oleksii.rotamanagementapp.domain.entities.Shift;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {MemberShiftMapper.class})
public interface ShiftMapper {

    @Mapping(source = "id", target = "shiftId")
    ShiftDto toShiftDTO(Shift shift);

}