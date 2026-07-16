package com.portfolio.changa_api.shared.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ShiftRequest(
        @NotBlank  String description,
        @NotNull @Future LocalDateTime dateTime,
        @NotBlank  String clientAddress,
        @NotBlank  String clientFullName,
        @NotBlank  String clientPhoneNumber,
        @NotBlank  String userUsername
) {
}
