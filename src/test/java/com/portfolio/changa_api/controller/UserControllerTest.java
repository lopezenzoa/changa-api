package com.portfolio.changa_api.controller;

import com.portfolio.changa_api.service.UserService;
import com.portfolio.changa_api.shared.dtos.RequestFacilityDTO;
import com.portfolio.changa_api.shared.dtos.RequestUserDTO;
import com.portfolio.changa_api.shared.dtos.ResponseFacilityDTO;
import com.portfolio.changa_api.shared.dtos.ResponseUserDTO;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.exceptions.NotFoundException;
import com.portfolio.changa_api.shared.exceptions.UniquenessViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

                .andExpect(jsonPath("$.facility.name").value(expected.facility().getName()))
                .andExpect(jsonPath("$.facility.description").value(expected.facility().getDescription()));
    }

    @Test
    void whenAddingAnUserWithAnyEmptyField_thenReturnA400WithNoBody() throws Exception {
        RequestUserDTO emptyRequest = new RequestUserDTO(
                "",
                "",
                "",
                "",
                "",
                "",
                new RequestFacilityDTO("", "")
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
        RequestUserDTO duplicatedRequest = new RequestUserDTO(
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