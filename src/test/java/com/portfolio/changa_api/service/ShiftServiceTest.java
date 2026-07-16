package com.portfolio.changa_api.service;

import com.portfolio.changa_api.repository.ShiftRepository;
import com.portfolio.changa_api.repository.UserRepository;
import com.portfolio.changa_api.shared.dtos.*;
import com.portfolio.changa_api.shared.enums.States;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ShiftServiceTest {
    @Autowired private ShiftService service;
    @Autowired private ShiftRepository repository;
    @Autowired private UserRepository userRepository;

    @Autowired private FacilityService facilityService;
    @Autowired private UserService userService;

    private ShiftRequest request;
    private RequestUserDTO userRequest;

    private ResponseUserDTO responseUser;

    @BeforeEach
    void setUp() {
        RequestFacilityDTO facilityRequest = new RequestFacilityDTO(
                "Facility I",
                "Facility I"
        );

        facilityService.add(facilityRequest);

        userRequest = new RequestUserDTO(
                "enzo",
                "enzo",
                "Enzo Agustín López",
                "3 de Febrero 5074",
                "223 600 4953",
                "LC-123456",
                facilityRequest
        );

        responseUser = userService.add(userRequest);

        request = new ShiftRequest(
                "Shift (Desc)",
                LocalDateTime.of(2026, 7, 31, 15, 0),
                "Client St.",
                "Client Full Name",
                "223 600",
                userRequest.username()
        );
    }

    @Test
    void whenRequestingAValidShift_thenReturnAShiftResponseDTO() {
         ShiftResponse result = service.request(request);

         ShiftResponse expected = new ShiftResponse(
                 1L,
                 "SHIFT (DESC)",
                 LocalDateTime.of(2026, 7, 31, 15, 0),
                 "CLIENT ST.",
                 "CLIENT FULL NAME",
                 "223 600",
                 States.PENDING,
                 responseUser
         );

         assertEquals(expected, result);
    }

    @Test
    void whenRequestingAShiftWithBlankOrNullFields_thenThrowAnInvalidRequestFieldException() {
        ShiftRequest nullRequest = new ShiftRequest(
                null,
                null,
                null,
                null,
                null,
                null
        );

        ShiftRequest blankRequest = new ShiftRequest(
                "",
                LocalDateTime.of(2026, 7, 31, 15, 0),
                "",
                "",
                "",
                userRequest.username()
        );

        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.request(nullRequest)
        );

        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.request(blankRequest)
        );
    }

    @Test
    void whenAcceptingAShift_ThenReturnAnUpdatedShiftResponse() {
        Long shiftId = 1L;

        service.request(request);

        ShiftResponse expected = new ShiftResponse(
                1L,
                "SHIFT (DESC)",
                LocalDateTime.of(2026, 7, 31, 15, 0),
                "CLIENT ST.",
                "CLIENT FULL NAME",
                "223 600",
                States.ACCEPTED,
                responseUser
        );

        ShiftResponse result = service.acceptShift(shiftId);

        assertEquals(expected, result);
    }

    @Test
    void whenAcceptingAShiftWithNullId_ThenThrowAnInvalidRequestFieldException() {
        service.request(request);

        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.acceptShift(null)
        );
    }

    @Test
    void whenAcceptingAShiftWithNonExistentId_ThenThrowANotFoundException() {
        service.request(request);

        assertThrows(
                NotFoundException.class,
                () -> service.acceptShift(1000L)
        );
    }

    @Test
    void whenCancellingAShift_ThenReturnAnUpdatedShiftResponse() {
        Long shiftId = 1L;

        service.request(request);

        ShiftResponse expected = new ShiftResponse(
                1L,
                "SHIFT (DESC)",
                LocalDateTime.of(2026, 7, 31, 15, 0),
                "CLIENT ST.",
                "CLIENT FULL NAME",
                "223 600",
                States.CANCELLED,
                responseUser
        );

        ShiftResponse result = service.cancelShift(shiftId);

        assertEquals(expected, result);
    }

    @Test
    void whenCancellingAShiftWithNullId_ThenThrowAnInvalidRequestFieldException() {
        service.request(request);

        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.cancelShift(null)
        );
    }

    @Test
    void whenCancellingAShiftWithNonExistentId_ThenThrowANotFoundException() {
        service.request(request);

        assertThrows(
                NotFoundException.class,
                () -> service.cancelShift(1000L)
        );
    }

    @Test
    void whenFinishingAShift_ThenReturnAnUpdatedShiftResponse() {
        Long shiftId = 1L;

        service.request(request);

        ShiftResponse expected = new ShiftResponse(
                1L,
                "SHIFT (DESC)",
                LocalDateTime.of(2026, 7, 31, 15, 0),
                "CLIENT ST.",
                "CLIENT FULL NAME",
                "223 600",
                States.FINISHED,
                responseUser
        );

        ShiftResponse result = service.finishShift(shiftId);

        assertEquals(expected, result);
    }

    @Test
    void whenFinishingAShiftWithNullId_ThenThrowAnInvalidRequestFieldException() {
        service.request(request);

        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.finishShift(null)
        );
    }

    @Test
    void whenFinishingAShiftWithNonExistentId_ThenThrowANotFoundException() {
        service.request(request);

        assertThrows(
                NotFoundException.class,
                () -> service.finishShift(1000L)
        );
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        repository.deleteAll();
    }
}