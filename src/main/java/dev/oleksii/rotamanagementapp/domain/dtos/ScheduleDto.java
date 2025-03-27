package dev.oleksii.rotamanagementapp.domain.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {

    private List<ShiftDto> shifts;

}
