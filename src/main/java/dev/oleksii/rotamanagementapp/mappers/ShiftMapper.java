package dev.oleksii.rotamanagementapp.mappers;

import dev.oleksii.rotamanagementapp.domain.dtos.ShiftDTO;
import dev.oleksii.rotamanagementapp.domain.entities.Shift;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {MemberShiftMapper.class})
public interface ShiftMapper {

    @Mappings({
            @Mapping(source = "id", target = "shiftId"),
            @Mapping(source = "name", target = "shiftName")
    })
    ShiftDTO toShiftDTO(Shift shift);

}