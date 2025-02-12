package dev.oleksii.rotamanagementapp.mappers;

import dev.oleksii.rotamanagementapp.domain.dtos.ScheduleDTO;
import dev.oleksii.rotamanagementapp.domain.entities.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ShiftMapper.class})
public interface ScheduleMapper {

    @Mapping(source = "id", target = "scheduleId")
    ScheduleDTO toScheduleDTO(Schedule schedule);

}