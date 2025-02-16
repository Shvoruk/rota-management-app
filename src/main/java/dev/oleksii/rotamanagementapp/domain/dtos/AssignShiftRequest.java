package dev.oleksii.rotamanagementapp.domain.dtos;

import jakarta.validation.constraints.NotNull;
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
public class AssignShiftRequest {

    @NotNull(message = "Member Id is required.")
    private UUID memberId;

    @NotNull(message = "Start time is required.")
    private LocalTime startTime;

    @NotNull(message = "End time is required.")
    private LocalTime endTime;

}
