package com.portfolio.changa_api.controller;

import com.portfolio.changa_api.service.UserService;
import com.portfolio.changa_api.shared.dtos.RequestUserDTO;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.exceptions.NotFoundException;
import com.portfolio.changa_api.shared.exceptions.UniquenessViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired private UserService service;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody RequestUserDTO request) {
        try {
            return ResponseEntity.ok(service.add(request));
        } catch (InvalidRequestFieldException | UniquenessViolationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/facility/{name}")
    public ResponseEntity<?> getByFacilityName(@PathVariable String name) {
        try {
            return ResponseEntity.ok(service.getByFacilityName(name));
        } catch (InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
