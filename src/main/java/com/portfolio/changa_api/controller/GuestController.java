package com.portfolio.changa_api.controller;

import com.portfolio.changa_api.service.GuestService;
import com.portfolio.changa_api.shared.dtos.GuestDTO;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guests")
public class GuestController {
    @Autowired private GuestService service;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody GuestDTO request) {
        try {
            return ResponseEntity.ok(service.add(request));
        } catch (InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
