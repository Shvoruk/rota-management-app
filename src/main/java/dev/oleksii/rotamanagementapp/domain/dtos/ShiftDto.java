package dev.oleksii.rotamanagementapp.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShiftDto {

    private UUID shiftId;

    private String name;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private List<MemberShiftDto> memberShifts;

}
