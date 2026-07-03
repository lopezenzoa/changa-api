package com.portfolio.changa_api.service;

import com.portfolio.changa_api.shared.builders.GuestBuilder;
import com.portfolio.changa_api.shared.dtos.GuestDTO;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GuestServiceTest {
    @Autowired private GuestService service;

    @Test
    void whenAddingAValidGuest_thenReturnAGuestDTO() {
        GuestDTO request = new GuestBuilder()
                .setFullName("Guest I")
                .setPhoneNumber("223 I")
                .setAddress("Guest St. I")
                .buildDTO();

        GuestDTO expected = new GuestBuilder()
                .setFullName("GUEST I")
                .setPhoneNumber("223 I")
                .setAddress("GUEST ST. I")
                .buildDTO();

        GuestDTO result = service.add(request);

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    void whenAddingAGuestWithNullOrBlank_thenThrowAnInvalidRequestFieldException() {
        GuestDTO nullRequest = new GuestBuilder()
                .setFullName(null)
                .setPhoneNumber(null)
                .setAddress(null)
                .buildDTO();

        GuestDTO blankRequest = new GuestBuilder()
                .setFullName("")
                .setPhoneNumber("")
                .setAddress("")
                .buildDTO();

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
    void whenAddingAnExistentGuest_thenReturnAGuestDTO() {
        GuestDTO request_1 = new GuestBuilder()
                .setFullName("Guest I")
                .setPhoneNumber("223 I")
                .setAddress("Guest St. I")
                .buildDTO();

        service.add(request_1);

        GuestDTO request_2 = new GuestBuilder()
                .setFullName("Guest I")
                .setPhoneNumber("223 I")
                .setAddress("Guest St. I")
                .buildDTO();

        GuestDTO expected = new GuestBuilder()
                .setFullName("GUEST I")
                .setPhoneNumber("223 I")
                .setAddress("GUEST ST. I")
                .buildDTO();

        GuestDTO result = service.add(request_2);

        assertNotNull(result);
        assertEquals(expected, result);
    }
}