package com.portfolio.changa_api.controller;

import com.portfolio.changa_api.service.UserService;
import com.portfolio.changa_api.shared.dtos.FacilityRequest;
import com.portfolio.changa_api.shared.dtos.UserRequest;
import com.portfolio.changa_api.shared.dtos.FacilityResponse;
import com.portfolio.changa_api.shared.dtos.UserResponse;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.exceptions.NotFoundException;
import com.portfolio.changa_api.shared.exceptions.UniquenessViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService service;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void whenAddingAValidUser_thenReturn200WithResponseUserDTO() throws Exception {
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

        when(service.add(request)).thenReturn(expected);

        mockMvc.perform(
                        post("/api/users/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value(expected.fullName()))
                .andExpect(jsonPath("$.address").value(expected.address()))
                .andExpect(jsonPath("$.phoneNumber").value(expected.phoneNumber()))
                .andExpect(jsonPath("$.licenseNumber").value(expected.licenseNumber()))

                .andExpect(jsonPath("$.facility.name").value(expected.facility().name()))
                .andExpect(jsonPath("$.facility.description").value(expected.facility().description()));
    }

    @Test
    void whenAddingAnUserWithAnyEmptyField_thenReturnA400WithNoBody() throws Exception {
        UserRequest emptyRequest = new UserRequest(
                "",
                "",
                "",
                "",
                "",
                "",
                new FacilityRequest("", "")
        );

        when(service.add(emptyRequest)).thenThrow(new InvalidRequestFieldException("ANY FIELD CAN'T BE EMPTY"));

        mockMvc.perform(
                        post("/api/users/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(emptyRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAddingADuplicatedUser_thenThrowAnUniquenessViolationException() throws Exception {
        UserRequest duplicatedRequest = new UserRequest(
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

        when(service.add(duplicatedRequest)).thenThrow(new UniquenessViolationException("USERNAME, PHONE NUMBER OR LICENSE NUMBER ALREADY IN USED"));

        mockMvc.perform(
                        post("/api/users/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(duplicatedRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGettingByFacilityName_thenReturnA200WithListOfUserResponseDTO() throws Exception {
        String facilityName = "Programador";

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
                                "PROGRAMDOR (DESC)",
                                1L
                        )
                )
        );

        when(service.getByFacilityName(facilityName)).thenReturn(expectedList);

        mockMvc.perform(
                        get("/api/users/facility/{name}", facilityName)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].fullName").value(expectedList.get(0).fullName()))
                .andExpect(jsonPath("$[1].fullName").value(expectedList.get(1).fullName()));
    }

    @Test
    void whenGettingByFacilityNameWithNoUsers_thenReturnA404WithNoBody() throws Exception {
        String facilityName = "Mecanico";

        when(service.getByFacilityName(facilityName)).thenThrow(new NotFoundException("FACILITY WITH NAME '" + facilityName.toUpperCase() + "' NOT FOUND"));

        mockMvc.perform(
                        get("/api/users/facility/{name}", facilityName)
                )
                .andExpect(status().isNotFound());
    }
}