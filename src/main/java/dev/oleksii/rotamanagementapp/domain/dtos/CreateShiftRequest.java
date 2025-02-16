package dev.oleksii.rotamanagementapp.domain.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateShiftRequest {

    @NotBlank(message = "Shift name is required.")
    private String name;

    @NotNull(message = "Date is required.")
    @FutureOrPresent(message = "Shift date must be today or in the future.")
    private LocalDate date;

    @NotNull(message = "Start time is required.")
    private LocalTime startTime;

    @NotNull(message = "End time is required.")
    private LocalTime endTime;

}
