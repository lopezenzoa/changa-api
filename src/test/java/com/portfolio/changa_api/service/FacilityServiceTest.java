package com.portfolio.changa_api.service;

import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.dtos.RequestFacilityDTO;
import com.portfolio.changa_api.shared.dtos.ResponseFacilityDTO;
import com.portfolio.changa_api.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FacilityServiceTest {
    @Autowired private FacilityService service;

    @Test
    void whenAddingAValidFacility_thenReturnAResponseFacilityDTO() {
        RequestFacilityDTO request = new RequestFacilityDTO(
                "Facility I",
                "Facility I"
        );

        ResponseFacilityDTO expected = new ResponseFacilityDTO(
                "FACILITY I",
                "FACILITY I",
                1L
        );

        ResponseFacilityDTO result = service.add(request);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    void whenAddingAFacilityWithNullOrBlank_thenThrowAnInvalidRequestFieldException() {
        RequestFacilityDTO nullRequest = new RequestFacilityDTO(
                null,
                null
        );

        RequestFacilityDTO blankRequest = new RequestFacilityDTO(
                "",
                ""
        );

        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.add(nullRequest)
        );

        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.add(blankRequest)
        );
    }

    @Test
    void whenAddingAnExistentFacility_thenReturnAResponseFacilityDTO() {
        RequestFacilityDTO request = new RequestFacilityDTO(
                "Facility I",
                "Facility I"
        );

        service.add(request);

        RequestFacilityDTO request_2 = new RequestFacilityDTO(
                "Facility I",
                "Facility I (Desc)"
        );

        ResponseFacilityDTO expected = new ResponseFacilityDTO(
                "FACILITY I",
                "FACILITY I",
                1L
        );

        ResponseFacilityDTO result = service.add(request_2);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    void whenGettingByValidName_thenReturnAResponseFacilityDTO() {
        RequestFacilityDTO request = new RequestFacilityDTO(
                "Facility I",
                "Facility I"
        );

        service.add(request);

        String validName = "Facility I";

        ResponseFacilityDTO expected = new ResponseFacilityDTO(
                "FACILITY I",
                "FACILITY I",
                1L
        );

        ResponseFacilityDTO result = service.getByName(validName);

        assertNotNull(result);
        assertEquals(result, expected);
    }

    @Test
    void whenGettingByNullOrBlankName_thenThrowAnInvalidRequestFieldException() {
        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.getByName(null)
        );

        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.getByName("")
        );
    }

    @Test
    void whenGettingByNonExistentName_thenThrowANotFoundException() {
        assertThrows(
                NotFoundException.class,
                () -> service.getByName("NonExistentName")
        );
    }

    @Test
    void whenDeletingByValidName_thenReturnTrue() {
        RequestFacilityDTO request = new RequestFacilityDTO(
                "Facility I",
                "Facility I"
        );

        service.add(request);

        String validName = "Facility I";

        Boolean result = service.deleteByName(validName);

        assertTrue(result);
        assertThrows(
                NotFoundException.class,
                () -> service.getByName(validName)
        );
    }

    @Test
    void whenDeletingByNullOrBlankName_thenThrowAnInvalidRequestFieldException() {
        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.deleteByName(null)
        );

        assertThrows(
                InvalidRequestFieldException.class,
                () -> service.deleteByName("")
        );
    }

    @Test
    void whenDeletingByNonExistentName_thenThrowANotFoundException() {
        assertThrows(
                NotFoundException.class,
                () -> service.deleteByName("NonExistentName")
        );
    }
}