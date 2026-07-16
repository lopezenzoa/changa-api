package com.portfolio.changa_api.controller;

import com.portfolio.changa_api.service.ShiftService;
import com.portfolio.changa_api.shared.dtos.ShiftRequest;
import com.portfolio.changa_api.shared.exceptions.InvalidRequestFieldException;
import com.portfolio.changa_api.shared.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shifts")
public class ShiftController {
    @Autowired private ShiftService service;

    @PostMapping("/request")
    public ResponseEntity<?> request(@RequestBody  ShiftRequest request) {
        try {
            return ResponseEntity.ok(service.request(request));
        } catch (InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/accept/{id}")
    public ResponseEntity<?> accept(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.acceptShift(id));
        } catch (InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.cancelShift(id));
        } catch (InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/finish/{id}")
    public ResponseEntity<?> finish(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.finishShift(id));
        } catch (InvalidRequestFieldException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
