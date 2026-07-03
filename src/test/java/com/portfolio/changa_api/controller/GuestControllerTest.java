package com.portfolio.changa_api.controller;

import com.portfolio.changa_api.service.GuestService;
import com.portfolio.changa_api.shared.builders.GuestBuilder;
import com.portfolio.changa_api.shared.dtos.GuestDTO;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GuestController.class)
@ExtendWith(MockitoExtension.class)
class GuestControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private GuestService service;
    @Autowired private ObjectMapper mapper;

    private GuestDTO buildRequest() {
        return new GuestBuilder()
                .setFullName("Guest I")
                .setPhoneNumber("223 I")
                .setAddress("Guest St. I")
                .buildDTO();
    }

    private GuestDTO buildExpected() {
        return new GuestBuilder()
                .setFullName("GUEST I")
                .setPhoneNumber("223 I")
                .setAddress("GUEST ST. I")
                .buildDTO();
    }

    @Test
    void whenAddingAValidGuest_thenReturnA200WithGuestDTO() throws Exception {
        GuestDTO request = buildRequest();
        GuestDTO expected = buildExpected();

        when(service.add(request)).thenReturn(expected);

        mockMvc.perform(
                        post("/api/guests/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value(expected.fullName()))
                .andExpect(jsonPath("$.phoneNumber").value(expected.phoneNumber()))
                .andExpect(jsonPath("$.address").value(expected.address()));
    }

    @Test
    void whenAddingAGuestWithAnyBlankField_thenReturnA400WithNoBody() throws Exception {
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

        when(service.add(nullRequest)).thenThrow(InvalidRequestFieldException.class);
        when(service.add(blankRequest)).thenThrow(InvalidRequestFieldException.class);

        mockMvc.perform(
                        post("/api/guests/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(nullRequest))
                )
                .andExpect(status().isBadRequest());

        mockMvc.perform(
                        post("/api/guests/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(blankRequest))
                )
                .andExpect(status().isBadRequest());
    }
}