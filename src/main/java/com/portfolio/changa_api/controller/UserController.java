package com.portfolio.changa_api.controller;

import com.portfolio.changa_api.service.UserService;
import com.portfolio.changa_api.shared.dtos.UserRequest;
import com.portfolio.changa_api.shared.dtos.UserResponse;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.exceptions.NotFoundException;
import com.portfolio.changa_api.shared.exceptions.UniquenessViolationException;
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

@Tag(name = "Users", description = "Endpoints for creating and retrieving users")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired private UserService service;

    @Operation(
            summary = "Add a new user",
            description = "Creates a new user linked to a facility, validating all required fields " +
                    "and enforcing uniqueness constraints (e.g. username, license)."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully created",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "One or more request fields are blank/null, or a uniqueness " +
                            "constraint (e.g. username already taken) was violated"
            )
    })
    @PostMapping("/add")
    public ResponseEntity<?> add(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the user being created",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserRequest.class))
            )
            @RequestBody
            UserRequest request) {
        try {
            return ResponseEntity.ok(service.add(request));
        } catch (InvalidRequestFieldException | UniquenessViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Get users by facility name",
            description = "Retrieves all users associated with the facility matching the given name."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Users successfully retrieved",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "The provided facility name is blank or invalid"),
            @ApiResponse(responseCode = "404", description = "No facility with the given name was found")
    })
    @GetMapping("/facility/{name}")
    public ResponseEntity<?> getByFacilityName(
            @Parameter(description = "Name of the facility to search users for", required = true, example = "Facility I")
            @PathVariable
            String name
    ) {
        try {
            return ResponseEntity.ok(service.getByFacilityName(name));
        } catch (InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
