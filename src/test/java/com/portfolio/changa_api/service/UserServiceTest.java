package com.portfolio.changa_api.service;

import com.portfolio.changa_api.shared.dtos.RequestFacilityDTO;
import com.portfolio.changa_api.shared.dtos.RequestUserDTO;
import com.portfolio.changa_api.shared.dtos.ResponseFacilityDTO;
import com.portfolio.changa_api.shared.dtos.ResponseUserDTO;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.exceptions.NotFoundException;
import com.portfolio.changa_api.shared.exceptions.UniquenessViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired private UserService service;

    @Test
    void whenAddingAValidUser_thenReturnAResponseUserDTO() {
        RequestUserDTO request = new RequestUserDTO(
                "enzo",
                "enzo",
                "Enzo Agustín López",
                "3 de Febrero 5074",
                "223 600 4953",
                "LC-123456",
                new RequestFacilityDTO(
                        "Programador",
                        "Programador (Desc)"
                )
        );

        ResponseUserDTO result = service.add(request);

        ResponseUserDTO expected = new ResponseUserDTO(
                "ENZO AGUSTÍN LÓPEZ",
                "3 DE FEBRERO 5074",
                "223 600 4953",
                "LC-123456",
                new ResponseFacilityDTO(
                        "PROGRAMADOR",
                        "PROGRAMDOR (DESC)",
                        1L
                )
        );

        assertEquals(expected, result);
    }

    @Test
    void whenAddingATwoValidUsers_thenReturnAResponseUserDTO() {
        RequestUserDTO request_1 = new RequestUserDTO(
                "enzo",
                "enzo",
                "Enzo Agustín López",
                "3 de Febrero 5074",
                "223 600 4953",
                "LC-123456",
                new RequestFacilityDTO(
                        "Programador",
                        "Programador (Desc)"
                )
        );

        RequestUserDTO request_2 = new RequestUserDTO(
                "enzo 2",
                "enzo 2",
                "Enzo Agustín López 2",
                "3 de Febrero 5074 2",
                "223 600 4953 2",
                "LC-123456 2",
                new RequestFacilityDTO(
                        "Programador",
                        "Programador (Desc)"
                )
        );

        ResponseUserDTO result_1 = service.add(request_1);
        ResponseUserDTO result_2 = service.add(request_2);

        ResponseUserDTO expected_1 = new ResponseUserDTO(
                "ENZO AGUSTÍN LÓPEZ",
                "3 DE FEBRERO 5074",
                "223 600 4953",
                "LC-123456",
                new ResponseFacilityDTO(
                        "PROGRAMADOR",
                        "PROGRAMDOR (DESC)",
                        1L
                )
        );

        ResponseUserDTO expected_2 = new ResponseUserDTO(
                "ENZO AGUSTÍN LÓPEZ 2",
                "3 DE FEBRERO 5074 2",
                "223 600 4953 2",
                "LC-123456 2",
                new ResponseFacilityDTO(
                        "PROGRAMADOR",
                        "PROGRAMDOR (DESC)",
                        1L
                )
        );

        assertEquals(expected_1, result_1);
        assertEquals(expected_2, result_2);
    }

    @Test
    void whenAddingAValidUserWithNoLicenseNumber_thenReturnAResponseUserDTOWithRandomLicenseNumber() {
        RequestUserDTO request = new RequestUserDTO(
                "enzo",
                "enzo",
                "Enzo Agustín López",
                "3 de Febrero 5074",
                "223 600 4953",
                "",
                new RequestFacilityDTO(
                        "Programador",
                        "Programador (Desc)"
                )
        );

        ResponseUserDTO result = service.add(request);

        ResponseUserDTO expected = new ResponseUserDTO(
                "ENZO AGUSTÍN LÓPEZ",
                "3 DE FEBRERO 5074",
                "223 600 4953",
                result.licenseNumber(),
                new ResponseFacilityDTO(
                        "PROGRAMADOR",
                        "PROGRAMDOR (DESC)",
                        1L
                )
        );

        assertEquals(expected, result);
    }

    @Test
    void whenAddingAnUserWithAnyEmptyField_thenThrowAnInvalidRequestFieldException() {
        RequestUserDTO request = new RequestUserDTO(
                "",
                "",
                "",
                "",
                "",
                "",
                new RequestFacilityDTO("", "")
        );

        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.add(request)
        );
    }

    @Test
    void whenAddingADuplicatedUser_thenThrowAnUniquenessViolationException() {
        RequestUserDTO request = new RequestUserDTO(
                "enzo",
                "enzo",
                "Enzo Agustín López",
                "3 de Febrero 5074",
                "223 600 4953",
                "",
                new RequestFacilityDTO(
                        "Programador",
                        "Programador (Desc)"
                )
        );

        ResponseUserDTO result = service.add(request);

        ResponseUserDTO expected = new ResponseUserDTO(
                "ENZO AGUSTÍN LÓPEZ",
                "3 DE FEBRERO 5074",
                "223 600 4953",
                result.licenseNumber(),
                new ResponseFacilityDTO(
                        "PROGRAMADOR",
                        "PROGRAMDOR (DESC)",
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
        RequestUserDTO request_1 = new RequestUserDTO(
                "enzo",
                "enzo",
                "Enzo Agustín López",
                "3 de Febrero 5074",
                "223 600 4953",
                "LC-123456",
                new RequestFacilityDTO(
                        "Programador",
                        "Programador (Desc)"
                )
        );

        RequestUserDTO request_2 = new RequestUserDTO(
                "enzo 2",
                "enzo 2",
                "Enzo Agustín López 2",
                "3 de Febrero 5074 2",
                "223 600 4953 2",
                "LC-123456 2",
                new RequestFacilityDTO(
                        "Programador",
                        "Programador (Desc)"
                )
        );

        service.add(request_1);
        service.add(request_2);

        List<ResponseUserDTO> resultList = service.getByFacilityName("Programador");

        List<ResponseUserDTO> expectedList = List.of(
                new ResponseUserDTO(
                        "ENZO AGUSTÍN LÓPEZ",
                        "3 DE FEBRERO 5074",
                        "223 600 4953",
                        "LC-123456",
                        new ResponseFacilityDTO(
                                "PROGRAMADOR",
                                "PROGRAMDOR (DESC)",
                                1L
                        )
                ),
                new ResponseUserDTO(
                        "ENZO AGUSTÍN LÓPEZ 2",
                        "3 DE FEBRERO 5074 2",
                        "223 600 4953 2",
                        "LC-123456 2",
                        new ResponseFacilityDTO(
                                "PROGRAMADOR",
                                "PROGRAMDOR (DESC)",
                                1L
                        )
                )
        );

        assertEquals(expectedList, resultList);
    }

    @Test
    void whenGettingByFacilityNameWithNoUsers_thenThrowANotFoundException() {
        RequestUserDTO request_1 = new RequestUserDTO(
                "enzo",
                "enzo",
                "Enzo Agustín López",
                "3 de Febrero 5074",
                "223 600 4953",
                "LC-123456",
                new RequestFacilityDTO(
                        "Programador",
                        "Programador (Desc)"
                )
        );

        RequestUserDTO request_2 = new RequestUserDTO(
                "enzo 2",
                "enzo 2",
                "Enzo Agustín López 2",
                "3 de Febrero 5074 2",
                "223 600 4953 2",
                "LC-123456 2",
                new RequestFacilityDTO(
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
}