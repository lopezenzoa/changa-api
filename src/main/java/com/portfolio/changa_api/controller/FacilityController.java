package com.portfolio.changa_api.controller;

import com.portfolio.changa_api.service.FacilityService;
import com.portfolio.changa_api.shared.dtos.RequestFacilityDTO;
import com.portfolio.changa_api.shared.dtos.ResponseFacilityDTO;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/facility")
@Tag(name = "Facility", description = "Endpoints for managing facilities")
public class FacilityController {
    @Autowired private FacilityService service;

    @Operation(summary = "Create a new facility")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Facility created successfully",
                    content = @Content(schema = @Schema(implementation = ResponseFacilityDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content)
    })
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody RequestFacilityDTO request) {
        try {
            return ResponseEntity.ok(service.add(request));
        } catch (InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get a facility by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Facility found",
                    content = @Content(schema = @Schema(implementation = ResponseFacilityDTO.class))),
            @ApiResponse(responseCode = "404", description = "Facility not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid name provided", content = @Content)
    })
    @GetMapping("/{name}")
    public ResponseEntity<?> getByName(
            @Parameter(description = "Name of the facility to retrieve", example = "Plumber")
            @PathVariable String name
    ) {
        try {
            return ResponseEntity.ok(service.getByName(name));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Delete a facility by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Facility deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Facility not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid name provided", content = @Content)
    })
    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> deleteByName(
            @Parameter(description = "Name of the facility to delete", example = "Plumber")
            @PathVariable String name
    ) {
        try {
            return ResponseEntity.ok(service.deleteByName(name));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
