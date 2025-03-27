package dev.oleksii.rotamanagementapp.mappers;

import dev.oleksii.rotamanagementapp.domain.dtos.ScheduleDto;
import dev.oleksii.rotamanagementapp.domain.entities.Schedule;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ShiftMapper.class})
public interface ScheduleMapper {

    ScheduleDto toScheduleDTO(Schedule schedule);

}