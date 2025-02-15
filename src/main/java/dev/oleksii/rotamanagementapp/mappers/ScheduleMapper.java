package dev.oleksii.rotamanagementapp.mappers;

import dev.oleksii.rotamanagementapp.domain.dtos.ScheduleDto;
import dev.oleksii.rotamanagementapp.domain.entities.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ShiftMapper.class})
public interface ScheduleMapper {

    @Mapping(source = "id", target = "scheduleId")
    ScheduleDto toScheduleDTO(Schedule schedule);

}