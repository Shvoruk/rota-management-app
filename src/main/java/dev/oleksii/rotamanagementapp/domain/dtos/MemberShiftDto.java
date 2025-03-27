package dev.oleksii.rotamanagementapp.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberShiftDto {

    private UUID memberShiftId;

    private LocalTime startTime;

    private LocalTime endTime;

    private MemberDto member;

}
