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
import com.portfolio.changa_api.shared.formatters.ShiftFormatter;
import com.portfolio.changa_api.shared.mappers.ShiftMapper;
import com.portfolio.changa_api.shared.validators.ShiftValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShiftService {
    @Autowired private ShiftRepository repository;
    @Autowired private UserRepository userRepository;

    @Autowired private ShiftValidator validator;
    @Autowired private ShiftMapper mapper;
    @Autowired private ShiftFormatter formatter;

    public ShiftResponse request(ShiftRequest request) throws InvalidRequestFieldException, NotFoundException {
        /* VALIDATING THE REQUEST */
        validator.validate(request);

        /* INITIAL FORMATTING */
        request = formatter.format(request);

        /* SEARCHING THE USER BY ITS USERNAME */
        Optional<User> user = userRepository.findByUsername(request.userUsername());

        if (user.isEmpty())
            throw new NotFoundException("USER '" + request.userUsername() + "' NOT FOUND");

        /* BUILDING THE ENTITY */
        Shift entity = mapper.toEntity(request, user.get(), States.PENDING);

        /* SAVING THE ENTITY */
        Shift saved = repository.save(entity);

        return mapper.toResponse(saved);
    }

    public ShiftResponse acceptShift(Long shiftId) throws InvalidRequestFieldException, NotFoundException {
        Shift entity = getById(shiftId);
        entity.setState(States.ACCEPTED);

        Shift updated = update(entity);

        return mapper.toResponse(updated);
    }

    public ShiftResponse cancelShift(Long shiftId) throws InvalidRequestFieldException, NotFoundException {
        Shift entity = getById(shiftId);
        entity.setState(States.CANCELLED);

        Shift updated = update(entity);

        return mapper.toResponse(updated);
    }

    public ShiftResponse finishShift(Long shiftId) throws InvalidRequestFieldException, NotFoundException {
        Shift entity = getById(shiftId);
        entity.setState(States.FINISHED);

        Shift updated = update(entity);

        return mapper.toResponse(updated);
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
}
