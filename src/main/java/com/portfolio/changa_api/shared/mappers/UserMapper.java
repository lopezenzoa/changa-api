package com.portfolio.changa_api.shared.mappers;

import com.portfolio.changa_api.model.User;
import com.portfolio.changa_api.shared.dtos.UserRequest;
import com.portfolio.changa_api.shared.dtos.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserMapper {
    @Autowired private FacilityMapper facilityMapper;

    public User toEntity(UserRequest request) {
        return new User(
                null,
                request.username(),
                request.password(),
                request.fullName(),
                request.address(),
                request.phoneNumber(),
                request.licenseNumber(),
                facilityMapper.toEntity(request.facility()),
                new ArrayList<>()
        );
    }

    public UserResponse toResponse(User entity) {
        return new UserResponse(
                entity.getFullName(),
                entity.getAddress(),
                entity.getPhoneNumber(),
                entity.getLicenseNumber(),
                facilityMapper.toResponse(entity.getFacility())
        );
    }
}
