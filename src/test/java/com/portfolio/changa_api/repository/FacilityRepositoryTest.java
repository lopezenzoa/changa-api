package com.portfolio.changa_api.repository;

import com.portfolio.changa_api.model.Facility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FacilityRepositoryTest {
    @Autowired private FacilityRepository repository;

    @Test
    void whenFindingByName_thenReturnAFacilityOptional() {
        Facility entity = new Facility(
                null,
                "Facility I",
                "Facility (Desc) I",
                new ArrayList<>()
        );

        repository.save(entity);

        Optional<Facility> result = repository.findByName("Facility I");

        assertTrue(result.isPresent());

        assertEquals(entity.getName(), result.get().getName());
        assertEquals(entity.getDescription(), result.get().getDescription());
    }

    @Test
    void whenFindingByNonExistentOrNullName_thenReturnAnEmptyFacilityOptional() {
        Facility entity = new Facility(
                null,
                "Facility II",
                "Facility (Desc) II",
                new ArrayList<>()
        );

        repository.save(entity);

        Optional<Facility> result = repository.findByName("Facility Non-Existent");
        Optional<Facility> result_2 = repository.findByName(null);

        assertTrue(result.isEmpty());
        assertTrue(result_2.isEmpty());
    }
}