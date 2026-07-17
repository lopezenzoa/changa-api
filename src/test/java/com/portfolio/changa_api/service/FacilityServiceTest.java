package com.portfolio.changa_api.service;

import com.portfolio.changa_api.repository.FacilityRepository;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.dtos.FacilityRequest;
import com.portfolio.changa_api.shared.dtos.FacilityResponse;
import com.portfolio.changa_api.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FacilityServiceTest {
    @Autowired private FacilityService service;
    @Autowired private FacilityRepository repository;

    @Test
    void whenAddingAValidFacility_thenReturnAResponseFacilityDTO() {
        FacilityRequest request = new FacilityRequest(
                "Facility I",
                "Facility I"
        );

        FacilityResponse expected = new FacilityResponse(
                "FACILITY I",
                "FACILITY I",
                1L
        );

        FacilityResponse result = service.add(request);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    void whenAddingAFacilityWithNullOrBlank_thenThrowAnInvalidRequestFieldException() {
        FacilityRequest nullRequest = new FacilityRequest(
                null,
                null
        );

        FacilityRequest blankRequest = new FacilityRequest(
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
        FacilityRequest request = new FacilityRequest(
                "Facility I",
                "Facility I"
        );

        service.add(request);

        FacilityRequest request_2 = new FacilityRequest(
                "Facility I",
                "Facility I (Desc)"
        );

        FacilityResponse expected = new FacilityResponse(
                "FACILITY I",
                "FACILITY I",
                1L
        );

        FacilityResponse result = service.add(request_2);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    void whenGettingByValidName_thenReturnAResponseFacilityDTO() {
        FacilityRequest request = new FacilityRequest(
                "Facility I",
                "Facility I"
        );

        service.add(request);

        String validName = "Facility I";

        FacilityResponse expected = new FacilityResponse(
                "FACILITY I",
                "FACILITY I",
                1L
        );

        FacilityResponse result = service.getByName(validName);

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
        FacilityRequest request = new FacilityRequest(
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

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }
}