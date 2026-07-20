package com.portfolio.changa_api.controller;

import com.portfolio.changa_api.service.FacilityService;
import com.portfolio.changa_api.shared.dtos.FacilityRequest;
import com.portfolio.changa_api.shared.dtos.FacilityResponse;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacilityController.class)
@ExtendWith(MockitoExtension.class)
class FacilityControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private FacilityService service;
    @Autowired private ObjectMapper mapper;

    @Test
    void whenAddingAValidFacility_thenReturnA200WithResponseFacilityDTO() throws Exception {
        FacilityRequest request = new FacilityRequest(
                "Facility I",
                "Facility I"
        );

        FacilityResponse expected = new FacilityResponse(
                "FACILITY I",
                "FACILITY I",
                1L
        );

        when(service.add(request)).thenReturn(expected);

        mockMvc.perform(
                post("/api/facility/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expected.name()))
                .andExpect(jsonPath("$.description").value(expected.description()));
    }

    @Test
    void whenAddingAFacilityNullOrBlank_thenReturnA400WithNoBody() throws Exception {
        FacilityRequest nullRequest = new FacilityRequest(
                null,
                null
        );

        FacilityRequest blankRequest = new FacilityRequest(
                "",
                ""
        );

        when(service.add(nullRequest)).thenThrow(InvalidRequestFieldException.class);
        when(service.add(blankRequest)).thenThrow(InvalidRequestFieldException.class);

        mockMvc.perform(
                        post("/api/facility/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(nullRequest))
                )
                .andExpect(status().isBadRequest());

        mockMvc.perform(
                        post("/api/facility/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(blankRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGettingByValidName_thenReturnA200ResponseFacilityDTO() throws Exception {
        String validName = "Facility I";

        FacilityResponse expected = new FacilityResponse(
                "FACILITY I",
                "FACILITY I",
                1L
        );

        when(service.getByName(validName)).thenReturn(expected);

        mockMvc.perform(get("/api/facility/" + validName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expected.name()))
                .andExpect(jsonPath("$.description").value(expected.description()));;
    }

    @Test
    void whenGettingByNonExistentName_thenReturnA404WithNoBody() throws Exception {
        String nonExistentName = "nonExistentName";

        when(service.getByName(nonExistentName)).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/api/facility/{name}", nonExistentName))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeletingByValidName_thenReturnA200WithNoBody() throws Exception {
        String validName = "Facility I";

        when(service.deleteByName(validName)).thenReturn(true);

        mockMvc.perform(delete("/api/facility/delete/{name}", validName))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeletingByNonExistentName_thenReturnA404WithTrue() throws Exception {
        String nonExistentName = "nonExistentName";

        when(service.deleteByName(nonExistentName)).thenThrow(NotFoundException.class);

        mockMvc.perform(delete("/api/facility/delete/{name}", nonExistentName))
                .andExpect(status().isNotFound());
    }
}