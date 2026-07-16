package com.portfolio.changa_api.repository;

import com.portfolio.changa_api.model.Shift;
import com.portfolio.changa_api.shared.enums.States;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Shift> findByState(States state);
}
