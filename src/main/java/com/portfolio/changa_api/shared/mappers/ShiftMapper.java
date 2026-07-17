package com.portfolio.changa_api.shared.mappers;

import com.portfolio.changa_api.model.Shift;
import com.portfolio.changa_api.model.User;
import com.portfolio.changa_api.shared.dtos.ShiftRequest;
import com.portfolio.changa_api.shared.dtos.ShiftResponse;
import com.portfolio.changa_api.shared.enums.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShiftMapper {
    @Autowired private UserMapper userMapper;

    public Shift toEntity(ShiftRequest request, User user, States state) {
        return new Shift(
                null,
                request.description(),
                request.dateTime(),
                request.clientAddress(),
                request.clientFullName(),
                request.clientPhoneNumber(),
                state,
                user
        );
    }

    public ShiftResponse toResponse(Shift entity) {
        return new ShiftResponse(
                entity.getId(),
                entity.getDescription(),
                entity.getDateTime(),
                entity.getClientAddress(),
                entity.getClientFullName(),
                entity.getClientPhoneNumber(),
                entity.getState(),
                userMapper.toResponse(entity.getUser())
        );
    }
}
