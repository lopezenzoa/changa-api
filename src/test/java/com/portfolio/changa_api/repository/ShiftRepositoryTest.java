package com.portfolio.changa_api.repository;

import com.portfolio.changa_api.model.Shift;
import com.portfolio.changa_api.shared.enums.States;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShiftRepositoryTest {
    @Autowired private ShiftRepository repository;

    @Test
    void whenFindingByDateBetween_thenReturnAListOfShifts() {
        Shift entity_1 = new Shift(
                null,
                "Shift (DESC) I",
                LocalDateTime.of(2026, 7, 31, 0, 0),
                "Shift St. I",
                "Shift Full Name",
                "Shift Phone Number",
                States.PENDING,
                null
        );

        Shift entity_2 = new Shift(
                null,
                "Shift (DESC) II",
                LocalDateTime.of(2026, 8, 15, 0, 0),
                "Shift St. II",
                "Shift Full Name II",
                "Shift Phone Number II",
                States.PENDING,
                null
        );

        Shift entity_3 = new Shift(
                null,
                "Shift (DESC) III",
                LocalDateTime.of(2026, 8, 31, 0, 0),
                "Shift St. III",
                "Shift Full Name III",
                "Shift Phone Number III",
                States.PENDING,
                null
        );

        repository.save(entity_1);
        repository.save(entity_2);
        repository.save(entity_3);

        List<Shift> result = repository.findByDateTimeBetween(
                LocalDateTime.of(2026, 7, 31, 0, 0),
                LocalDateTime.of(2026, 8, 31, 0, 0)
        );

        assertFalse(result.isEmpty());

        assertEquals(result.get(0), entity_1);
        assertEquals(result.get(1), entity_2);
        assertEquals(result.get(2), entity_3);
    }

    @Test
    void whenFindingByDateBetweenWithNoShifts_thenReturnAnEmptyListOfShifts() {
        List<Shift> result = repository.findByDateTimeBetween(
                LocalDateTime.of(2026, 7, 31, 0, 0),
                LocalDateTime.of(2026, 8, 31, 0, 0)
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void whenFindingByState_thenReturnAListOfShifts() {
        Shift entity_1 = new Shift(
                null,
                "Shift (DESC) I",
                LocalDateTime.of(2026, 7, 31, 0, 0),
                "Shift St. I",
                "Shift Full Name",
                "Shift Phone Number",
                States.PENDING,
                null
        );

        Shift entity_2 = new Shift(
                null,
                "Shift (DESC) II",
                LocalDateTime.of(2026, 8, 15, 0, 0),
                "Shift St. II",
                "Shift Full Name II",
                "Shift Phone Number II",
                States.PENDING,
                null
        );

        Shift entity_3 = new Shift(
                null,
                "Shift (DESC) III",
                LocalDateTime.of(2026, 8, 31, 0, 0),
                "Shift St. III",
                "Shift Full Name III",
                "Shift Phone Number III",
                States.PENDING,
                null
        );

        repository.save(entity_1);
        repository.save(entity_2);
        repository.save(entity_3);

        List<Shift> result = repository.findByState(States.PENDING);

        assertFalse(result.isEmpty());

        assertEquals(result.get(0), entity_1);
        assertEquals(result.get(1), entity_2);
        assertEquals(result.get(2), entity_3);
    }

    @Test
    void whenFindingByStateWithNoShifts_thenReturnAnEmptyListOfShifts() {
        List<Shift> result = repository.findByDateTimeBetween(
                LocalDateTime.of(2026, 7, 31, 0, 0),
                LocalDateTime.of(2026, 8, 31, 0, 0)
        );

        assertTrue(result.isEmpty());
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }
}