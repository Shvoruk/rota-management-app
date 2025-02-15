package dev.oleksii.rotamanagementapp.domain.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {

    private UUID scheduleId;

    private List<ShiftDto> shifts;

}
