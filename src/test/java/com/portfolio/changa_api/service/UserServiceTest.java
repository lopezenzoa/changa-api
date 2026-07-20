package com.portfolio.changa_api.service;

import com.portfolio.changa_api.repository.UserRepository;
import com.portfolio.changa_api.shared.dtos.FacilityRequest;
import com.portfolio.changa_api.shared.dtos.UserRequest;
import com.portfolio.changa_api.shared.dtos.FacilityResponse;
import com.portfolio.changa_api.shared.dtos.UserResponse;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.exceptions.NotFoundException;
import com.portfolio.changa_api.shared.exceptions.UniquenessViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {
    @Autowired private UserService service;
    @Autowired private UserRepository repository;

    @Test
    void whenAddingAValidUser_thenReturnAResponseUserDTO() {
        UserRequest request = new UserRequest(
                "enzo",
                "enzo",
                "Enzo Agustín López",
                "3 de Febrero 5074",
                "223 600 4953",
                "LC-123456",
                new FacilityRequest(
                        "Programador",
                        "Programador (Desc)"
                )
        );

        UserResponse result = service.add(request);

        UserResponse expected = new UserResponse(
                "ENZO AGUSTÍN LÓPEZ",
                "3 DE FEBRERO 5074",
                "223 600 4953",
                "LC-123456",
                new FacilityResponse(
                        "PROGRAMADOR",
                        "PROGRAMADOR (DESC)",
                        1L
                )
        );

        assertEquals(expected, result);
    }

    @Test
    void whenAddingATwoValidUsers_thenReturnAResponseUserDTO() {
        UserRequest request_1 = new UserRequest(
                "enzo",
                "enzo",
                "Enzo Agustín López",
                "3 de Febrero 5074",
                "223 600 4953",
                "LC-123456",
                new FacilityRequest(
                        "Programador",
                        "Programador (Desc)"
                )
        );

        UserRequest request_2 = new UserRequest(
                "enzo 2",
                "enzo 2",
                "Enzo Agustín López 2",
                "3 de Febrero 5074 2",
                "223 600 4953 2",
                "LC-123456 2",
                new FacilityRequest(
                        "Programador",
                        "Programador (Desc)"
                )
        );

        UserResponse result_1 = service.add(request_1);
        UserResponse result_2 = service.add(request_2);

        UserResponse expected_1 = new UserResponse(
                "ENZO AGUSTÍN LÓPEZ",
                "3 DE FEBRERO 5074",
                "223 600 4953",
                "LC-123456",
                new FacilityResponse(
                        "PROGRAMADOR",
                        "PROGRAMADOR (DESC)",
                        1L
                )
        );

        UserResponse expected_2 = new UserResponse(
                "ENZO AGUSTÍN LÓPEZ 2",
                "3 DE FEBRERO 5074 2",
                "223 600 4953 2",
                "LC-123456 2",
                new FacilityResponse(
                        "PROGRAMADOR",
                        "PROGRAMADOR (DESC)",
                        1L
                )
        );

        assertEquals(expected_1, result_1);
        assertEquals(expected_2, result_2);
    }

    @Test
    void whenAddingAValidUserWithNoLicenseNumber_thenReturnAResponseUserDTOWithRandomLicenseNumber() {
        UserRequest request = new UserRequest(
                "enzo",
                "enzo",
                "Enzo Agustín López",
                "3 de Febrero 5074",
                "223 600 4953",
                "",
                new FacilityRequest(
                        "Programador",
                        "Programador (Desc)"
                )
        );

        UserResponse result = service.add(request);

        UserResponse expected = new UserResponse(
                "ENZO AGUSTÍN LÓPEZ",
                "3 DE FEBRERO 5074",
                "223 600 4953",
                result.licenseNumber(),
                new FacilityResponse(
                        "PROGRAMADOR",
                        "PROGRAMADOR (DESC)",
                        1L
                )
        );

        assertEquals(expected, result);
    }

    @Test
    void whenAddingAnUserWithAnyEmptyField_thenThrowAnInvalidRequestFieldException() {
        UserRequest request = new UserRequest(
                "",
                "",
                "",
                "",
                "",
                "",
                new FacilityRequest("", "")
        );

        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.add(request)
        );
    }

    @Test
    void whenAddingADuplicatedUser_thenThrowAnUniquenessViolationException() {
        UserRequest request = new UserRequest(
                "enzo",
                "enzo",
                "Enzo Agustín López",
                "3 de Febrero 5074",
                "223 600 4953",
                "",
                new FacilityRequest(
                        "Programador",
                        "Programador (Desc)"
                )
        );

        UserResponse result = service.add(request);

        UserResponse expected = new UserResponse(
                "ENZO AGUSTÍN LÓPEZ",
                "3 DE FEBRERO 5074",
                "223 600 4953",
                result.licenseNumber(),
                new FacilityResponse(
                        "PROGRAMADOR",
                        "PROGRAMADOR (DESC)",
                        1L
                )
        );

        assertEquals(expected, result);

        assertThrows(
                UniquenessViolationException.class,
                () -> service.add(request)
        );
    }

    @Test
    void whenGettingByFacilityName_thenReturnAListOfUserResponseDTO() {
        UserRequest request_1 = new UserRequest(
                "enzo",
                "enzo",
                "Enzo Agustín López",
                "3 de Febrero 5074",
                "223 600 4953",
                "LC-123456",
                new FacilityRequest(
                        "Programador",
                        "Programador (Desc)"
                )
        );

        UserRequest request_2 = new UserRequest(
                "enzo 2",
                "enzo 2",
                "Enzo Agustín López 2",
                "3 de Febrero 5074 2",
                "223 600 4953 2",
                "LC-123456 2",
                new FacilityRequest(
                        "Programador",
                        "Programador (Desc)"
                )
        );

        service.add(request_1);
        service.add(request_2);

        List<UserResponse> resultList = service.getByFacilityName("Programador");

        List<UserResponse> expectedList = List.of(
                new UserResponse(
                        "ENZO AGUSTÍN LÓPEZ",
                        "3 DE FEBRERO 5074",
                        "223 600 4953",
                        "LC-123456",
                        new FacilityResponse(
                                "PROGRAMADOR",
                                "PROGRAMADOR (DESC)",
                                1L
                        )
                ),
                new UserResponse(
                        "ENZO AGUSTÍN LÓPEZ 2",
                        "3 DE FEBRERO 5074 2",
                        "223 600 4953 2",
                        "LC-123456 2",
                        new FacilityResponse(
                                "PROGRAMADOR",
                                "PROGRAMADOR (DESC)",
                                1L
                        )
                )
        );

        assertEquals(expectedList, resultList);
    }

    @Test
    void whenGettingByFacilityNameWithNoUsers_thenThrowANotFoundException() {
        UserRequest request_1 = new UserRequest(
                "enzo",
                "enzo",
                "Enzo Agustín López",
                "3 de Febrero 5074",
                "223 600 4953",
                "LC-123456",
                new FacilityRequest(
                        "Programador",
                        "Programador (Desc)"
                )
        );

        UserRequest request_2 = new UserRequest(
                "enzo 2",
                "enzo 2",
                "Enzo Agustín López 2",
                "3 de Febrero 5074 2",
                "223 600 4953 2",
                "LC-123456 2",
                new FacilityRequest(
                        "Programador",
                        "Programador (Desc)"
                )
        );

        service.add(request_1);
        service.add(request_2);

        /* THIS IS BECAUSE THERE WILL NEVER BE FACILITIES WITHOUT USERS */
        assertThrows(
                NotFoundException.class,
                () -> service.getByFacilityName("Mecanico")
        );
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }
}