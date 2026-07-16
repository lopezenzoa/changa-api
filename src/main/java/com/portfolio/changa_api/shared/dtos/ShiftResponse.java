package com.portfolio.changa_api.shared.dtos;

import com.portfolio.changa_api.shared.enums.States;

import java.time.LocalDateTime;

public record ShiftResponse(
        Long id,
        String description,
        LocalDateTime dateTime,
        String clientAddress,
        String clientFullName,
        String clientPhoneNumber,
        States state,
        ResponseUserDTO user
) {
}
