package com.portfolio.changa_api.controller;

import com.portfolio.changa_api.service.ShiftService;
import com.portfolio.changa_api.shared.dtos.ShiftRequest;
import com.portfolio.changa_api.shared.dtos.ShiftResponse;
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

@Tag(name = "Shifts", description = "Endpoints for requesting and managing work shifts")
@RestController
@RequestMapping("/api/shifts")
public class ShiftController {
    @Autowired private ShiftService service;

    @Operation(
            summary = "Request a new shift",
            description = "Creates a new shift request on behalf of a user, validating all required fields."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Shift successfully requested",
                    content = @Content(schema = @Schema(implementation = ShiftResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "One or more request fields are blank, null, or invalid",
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Referenced user or facility was not found"
            )
    })
    @PostMapping("/request")
    public ResponseEntity<?> request(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the shift being requested",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ShiftRequest.class))
            )
            @RequestBody
            ShiftRequest request
    ) {
        try {
            return ResponseEntity.ok(service.request(request));
        } catch (InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Accept a shift",
            description = "Marks an existing shift as accepted, identified by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Shift successfully accepted",
                    content = @Content(schema = @Schema(implementation = ShiftResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Shift cannot be accepted in its current state"),
            @ApiResponse(responseCode = "404", description = "Shift with the given ID was not found")
    })
    @PutMapping("/accept/{id}")
    public ResponseEntity<?> accept(
            @Parameter(description = "ID of the shift to accept", required = true, example = "1")
            @PathVariable
            Long id
    ) {
        try {
            return ResponseEntity.ok(service.acceptShift(id));
        } catch (InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Cancel a shift",
            description = "Cancels an existing shift, identified by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Shift successfully cancelled",
                    content = @Content(schema = @Schema(implementation = ShiftResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Shift cannot be cancelled in its current state"),
            @ApiResponse(responseCode = "404", description = "Shift with the given ID was not found")
    })
    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancel(
            @Parameter(description = "ID of the shift to cancel", required = true, example = "1")
            @PathVariable
            Long id
    ) {
        try {
            return ResponseEntity.ok(service.cancelShift(id));
        } catch (InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Finish a shift",
            description = "Marks an existing shift as finished, identified by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Shift successfully finished",
                    content = @Content(schema = @Schema(implementation = ShiftResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Shift cannot be finished in its current state"),
            @ApiResponse(responseCode = "404", description = "Shift with the given ID was not found")
    })
    @PutMapping("/finish/{id}")
    public ResponseEntity<?> finish(
            @Parameter(description = "ID of the shift to finish", required = true, example = "1")
            @PathVariable
            Long id
    ) {
        try {
            return ResponseEntity.ok(service.finishShift(id));
        } catch (InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
