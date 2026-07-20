package com.portfolio.changa_api.shared.dtos;

import com.portfolio.changa_api.shared.enums.States;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

public record ShiftResponse(
        @NotNull @PositiveOrZero Long id,
        @NotBlank String description,
        @NotNull @Future LocalDateTime dateTime,
        @NotBlank String clientAddress,
        @NotBlank String clientFullName,
        @NotBlank String clientPhoneNumber,
        @NotBlank States state,
        @NotBlank UserResponse user
) {
}
