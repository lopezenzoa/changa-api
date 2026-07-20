package com.portfolio.changa_api.controller;

import com.portfolio.changa_api.service.ShiftService;
import com.portfolio.changa_api.shared.dtos.*;
import com.portfolio.changa_api.shared.enums.States;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShiftController.class)
@ExtendWith(MockitoExtension.class)
class ShiftControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;
    @MockitoBean private ShiftService service;

    @Test
    void whenRequestingAValidShift_thenReturn200WithShiftResponse() throws Exception {
        ShiftRequest request = new ShiftRequest(
                "Shift (Desc)",
                LocalDateTime.of(2026, 7, 31, 15, 0),
                "Client St.",
                "Client Full Name",
                "223 600",
                "enzo"
        );

        ShiftResponse expected = new ShiftResponse(
                1L,
                "SHIFT (DESC)",
                LocalDateTime.of(2026, 7, 31, 15, 0, 0),
                "CLIENT ST.",
                "CLIENT FULL NAME",
                "223 600",
                States.PENDING,
                new UserResponse(
                        "ENZO AGUSTÍN LÓPEZ",
                        "3 DE FEBRERO 5074",
                        "223 600 4953",
                        "LC-123456",
                        new FacilityResponse(
                                "FACILITY I",
                                "FACILITY I (DESC)",
                                1L
                        )
                )
        );

        when(service.request(request)).thenReturn(expected);

        mockMvc.perform(
                        post("/api/shifts/request")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(expected.description()))
                .andExpect(jsonPath("$.clientAddress").value(expected.clientAddress()))
                .andExpect(jsonPath("$.clientFullName").value(expected.clientFullName()))
                .andExpect(jsonPath("$.clientPhoneNumber").value(expected.clientPhoneNumber()))
                .andExpect(jsonPath("$.state").value(expected.state().toString()))

                .andExpect(jsonPath("$.user.fullName").value(expected.user().fullName()))
                .andExpect(jsonPath("$.user.address").value(expected.user().address()))
                .andExpect(jsonPath("$.user.phoneNumber").value(expected.user().phoneNumber()))
                .andExpect(jsonPath("$.user.licenseNumber").value(expected.user().licenseNumber()))

                .andExpect(jsonPath("$.user.facility.name").value(expected.user().facility().name()))
                .andExpect(jsonPath("$.user.facility.description").value(expected.user().facility().description()));
    }

    @Test
    void whenRequestingAShiftWithNullOrBlankFields_thenReturn400WithErrorMessage() throws Exception {
        ShiftRequest nullRequest = new ShiftRequest(
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(service.request(nullRequest)).thenThrow(new InvalidRequestFieldException("ANY FIELD CAN'T BE BLANK"));

        mockMvc.perform(
                        post("/api/shifts/request")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(nullRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("ANY FIELD CAN'T BE BLANK"));
    }

    @Test
    void whenRequestingAShiftWithAnNonExistentUser_thenReturn404WithNoBody() throws Exception {
        String nonExistentUsername = "nonExistentUsername";

        ShiftRequest request = new ShiftRequest(
                "Shift (Desc)",
                LocalDateTime.of(2026, 7, 31, 15, 0),
                "Client St.",
                "Client Full Name",
                "223 600",
                nonExistentUsername
        );

        when(service.request(request)).thenThrow(NotFoundException.class);

        mockMvc.perform(
                        post("/api/shifts/request")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void whenAcceptingAShift_thenReturn200WithUpdatedShiftResponse() throws Exception {
        Long shiftId = 1L;

        ShiftResponse expected = new ShiftResponse(
                1L,
                "SHIFT (DESC)",
                LocalDateTime.of(2026, 7, 31, 15, 0, 0),
                "CLIENT ST.",
                "CLIENT FULL NAME",
                "223 600",
                States.ACCEPTED,
                new UserResponse(
                        "ENZO AGUSTÍN LÓPEZ",
                        "3 DE FEBRERO 5074",
                        "223 600 4953",
                        "LC-123456",
                        new FacilityResponse(
                                "FACILITY I",
                                "FACILITY I (DESC)",
                                1L
                        )
                )
        );

        when(service.acceptShift(shiftId)).thenReturn(expected);

        mockMvc.perform(put("/api/shifts/accept/{id}", shiftId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(expected.description()))
                .andExpect(jsonPath("$.clientAddress").value(expected.clientAddress()))
                .andExpect(jsonPath("$.clientFullName").value(expected.clientFullName()))
                .andExpect(jsonPath("$.clientPhoneNumber").value(expected.clientPhoneNumber()))
                .andExpect(jsonPath("$.state").value(expected.state().toString()))

                .andExpect(jsonPath("$.user.fullName").value(expected.user().fullName()))
                .andExpect(jsonPath("$.user.address").value(expected.user().address()))
                .andExpect(jsonPath("$.user.phoneNumber").value(expected.user().phoneNumber()))
                .andExpect(jsonPath("$.user.licenseNumber").value(expected.user().licenseNumber()))

                .andExpect(jsonPath("$.user.facility.name").value(expected.user().facility().name()))
                .andExpect(jsonPath("$.user.facility.description").value(expected.user().facility().description()));
    }

    @Test
    void whenAcceptingAShiftWithNonExistentId_thenReturn404WithNoBody() throws Exception {
        Long nonExistentId = 1000L;

        when(service.acceptShift(nonExistentId)).thenThrow(NotFoundException.class);

        mockMvc.perform(put("/api/shifts/accept/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenCancellingAShift_thenReturn200WithUpdatedShiftResponse() throws Exception {
        Long shiftId = 1L;

        ShiftResponse expected = new ShiftResponse(
                1L,
                "SHIFT (DESC)",
                LocalDateTime.of(2026, 7, 31, 15, 0, 0),
                "CLIENT ST.",
                "CLIENT FULL NAME",
                "223 600",
                States.CANCELLED,
                new UserResponse(
                        "ENZO AGUSTÍN LÓPEZ",
                        "3 DE FEBRERO 5074",
                        "223 600 4953",
                        "LC-123456",
                        new FacilityResponse(
                                "FACILITY I",
                                "FACILITY I (DESC)",
                                1L
                        )
                )
        );

        when(service.cancelShift(shiftId)).thenReturn(expected);

        mockMvc.perform(put("/api/shifts/cancel/{id}", shiftId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(expected.description()))
                .andExpect(jsonPath("$.clientAddress").value(expected.clientAddress()))
                .andExpect(jsonPath("$.clientFullName").value(expected.clientFullName()))
                .andExpect(jsonPath("$.clientPhoneNumber").value(expected.clientPhoneNumber()))
                .andExpect(jsonPath("$.state").value(expected.state().toString()))

                .andExpect(jsonPath("$.user.fullName").value(expected.user().fullName()))
                .andExpect(jsonPath("$.user.address").value(expected.user().address()))
                .andExpect(jsonPath("$.user.phoneNumber").value(expected.user().phoneNumber()))
                .andExpect(jsonPath("$.user.licenseNumber").value(expected.user().licenseNumber()))

                .andExpect(jsonPath("$.user.facility.name").value(expected.user().facility().name()))
                .andExpect(jsonPath("$.user.facility.description").value(expected.user().facility().description()));
    }

    @Test
    void whenCancellingAShiftWithNonExistentId_thenReturn404WithNoBody() throws Exception {
        Long nonExistentId = 1000L;

        when(service.cancelShift(nonExistentId)).thenThrow(NotFoundException.class);

        mockMvc.perform(put("/api/shifts/cancel/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenFinishingAShift_thenReturn200WithUpdatedShiftResponse() throws Exception {
        Long shiftId = 1L;

        ShiftResponse expected = new ShiftResponse(
                1L,
                "SHIFT (DESC)",
                LocalDateTime.of(2026, 7, 31, 15, 0, 0),
                "CLIENT ST.",
                "CLIENT FULL NAME",
                "223 600",
                States.FINISHED,
                new UserResponse(
                        "ENZO AGUSTÍN LÓPEZ",
                        "3 DE FEBRERO 5074",
                        "223 600 4953",
                        "LC-123456",
                        new FacilityResponse(
                                "FACILITY I",
                                "FACILITY I (DESC)",
                                1L
                        )
                )
        );

        when(service.finishShift(shiftId)).thenReturn(expected);

        mockMvc.perform(put("/api/shifts/finish/{id}", shiftId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(expected.description()))
                .andExpect(jsonPath("$.clientAddress").value(expected.clientAddress()))
                .andExpect(jsonPath("$.clientFullName").value(expected.clientFullName()))
                .andExpect(jsonPath("$.clientPhoneNumber").value(expected.clientPhoneNumber()))
                .andExpect(jsonPath("$.state").value(expected.state().toString()))

                .andExpect(jsonPath("$.user.fullName").value(expected.user().fullName()))
                .andExpect(jsonPath("$.user.address").value(expected.user().address()))
                .andExpect(jsonPath("$.user.phoneNumber").value(expected.user().phoneNumber()))
                .andExpect(jsonPath("$.user.licenseNumber").value(expected.user().licenseNumber()))

                .andExpect(jsonPath("$.user.facility.name").value(expected.user().facility().name()))
                .andExpect(jsonPath("$.user.facility.description").value(expected.user().facility().description()));
    }

    @Test
    void whenFinishingAShiftWithNonExistentId_thenReturn404WithNoBody() throws Exception {
        Long nonExistentId = 1000L;

        when(service.finishShift(nonExistentId)).thenThrow(NotFoundException.class);

        mockMvc.perform(put("/api/shifts/finish/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }
}