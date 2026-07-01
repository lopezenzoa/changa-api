package com.portfolio.changa_api.controller;

import com.portfolio.changa_api.service.FacilityService;
import com.portfolio.changa_api.shared.dtos.RequestFacilityDTO;
import com.portfolio.changa_api.shared.dtos.ResponseFacilityDTO;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/facility")
public class FacilityController {
    @Autowired private FacilityService service;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody RequestFacilityDTO request) {
        try {
            return ResponseEntity.ok(service.add(request));
        } catch (InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getByName(@PathVariable String name) {
        try {
            return ResponseEntity.ok(service.getByName(name));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> deleteByName(@PathVariable String name) {
        try {
            return ResponseEntity.ok(service.deleteByName(name));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
