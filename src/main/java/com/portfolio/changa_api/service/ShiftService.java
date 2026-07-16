package com.portfolio.changa_api.service;

import com.portfolio.changa_api.model.Shift;
import com.portfolio.changa_api.model.User;
import com.portfolio.changa_api.repository.ShiftRepository;
import com.portfolio.changa_api.repository.UserRepository;
import com.portfolio.changa_api.shared.dtos.ShiftRequest;
import com.portfolio.changa_api.shared.dtos.ShiftResponse;
import com.portfolio.changa_api.shared.enums.States;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.exceptions.NotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class ShiftService {
    @Autowired private ShiftRepository repository;
    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;

    @Autowired private Validator validator;

    public ShiftResponse request(ShiftRequest request) throws InvalidRequestFieldException, NotFoundException {
        /* VALIDATING THE REQUEST */
        validateRequest(request);

        /* INITIAL FORMATTING */
        request = formatRequest(request);

        /* SEARCHING THE USER BY ITS USERNAME */
        Optional<User> user = userRepository.findByUsername(request.userUsername());

        if (user.isEmpty())
            throw new NotFoundException("USER '" + request.userUsername() + "' NOT FOUND");

        /* BUILDING THE ENTITY */
        Shift entity = mapToEntity(request, user.get(), States.PENDING);

        /* SAVING THE ENTITY */
        Shift saved = repository.save(entity);

        return mapToResponse(saved);
    }

    public ShiftResponse acceptShift(Long shiftId) throws InvalidRequestFieldException, NotFoundException {
        Shift entity = getById(shiftId);
        entity.setState(States.ACCEPTED);

        Shift updated = update(entity);

        return mapToResponse(updated);
    }

    public ShiftResponse cancelShift(Long shiftId) throws InvalidRequestFieldException, NotFoundException {
        Shift entity = getById(shiftId);
        entity.setState(States.CANCELLED);

        Shift updated = update(entity);

        return mapToResponse(updated);
    }

    public ShiftResponse finishShift(Long shiftId) throws InvalidRequestFieldException, NotFoundException {
        Shift entity = getById(shiftId);
        entity.setState(States.FINISHED);

        Shift updated = update(entity);

        return mapToResponse(updated);
    }

    private void validateRequest(ShiftRequest request) throws InvalidRequestFieldException {
        Set<ConstraintViolation<ShiftRequest>> violations = validator.validate(request);

        if (!violations.isEmpty())
            throw new InvalidRequestFieldException("ANY FIELD CAN'T BE BLANK");
    }

    private Shift getById(Long shiftId) throws InvalidRequestFieldException, NotFoundException {
        /* INITIAL VALIDATION */
        if (shiftId == null)
            throw new InvalidRequestFieldException("SHIFT ID CAN'T BE NULL");

        /* SEARCHING THE SHIFT */
        Optional<Shift> shiftOptional = repository.findById(shiftId);

        if (shiftOptional.isEmpty())
            throw new NotFoundException("SHIFT NOT FOUND");

        return shiftOptional.get();
    }

    private Shift update(Shift entity) throws InvalidRequestFieldException, NotFoundException {
        /* SEARCHING THE SHIFT */
        return repository.save(entity);
    }

    private ShiftRequest formatRequest(ShiftRequest request) {
        return new ShiftRequest(
                request.description().trim().toUpperCase(),
                request.dateTime(),
                request.clientAddress().trim().toUpperCase(),
                request.clientFullName().trim().toUpperCase(),
                request.clientPhoneNumber().trim().toUpperCase(),
                request.userUsername().trim().toUpperCase()
        );
    }

    private Shift mapToEntity(ShiftRequest request, User user, States state) {
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

    private ShiftResponse mapToResponse(Shift entity) {
        return new ShiftResponse(
                entity.getId(),
                entity.getDescription(),
                entity.getDateTime(),
                entity.getClientAddress(),
                entity.getClientFullName(),
                entity.getClientPhoneNumber(),
                entity.getState(),
                userService.mapToResponse(entity.getUser())
        );
    }
}
