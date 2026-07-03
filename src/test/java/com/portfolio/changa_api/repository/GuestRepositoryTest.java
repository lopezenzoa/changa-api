package com.portfolio.changa_api.repository;

import com.portfolio.changa_api.model.Guest;
import com.portfolio.changa_api.model.Guest;
import com.portfolio.changa_api.shared.builders.GuestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GuestRepositoryTest {
    @Autowired
    private GuestRepository repository;

    @Test
    void whenFindingByName_thenReturnAGuestOptional() {
        Guest entity = new GuestBuilder()
                .setId(null)
                .setFullName("Guest I")
                .setPhoneNumber("223 I")
                .setAddress("Guest St. I")
                .buildEntity();

        repository.save(entity);

        Optional<Guest> result = repository.findByFullName("Guest I");

        assertTrue(result.isPresent());

        assertEquals(entity.getFullName(), result.get().getFullName());
        assertEquals(entity.getPhoneNumber(), result.get().getPhoneNumber());
        assertEquals(entity.getAddress(), result.get().getAddress());
    }

    @Test
    void whenFindingByNonExistentOrNullName_thenReturnAnEmptyGuestOptional() {
        Guest entity = new GuestBuilder()
                .setId(null)
                .setFullName("Guest I")
                .setPhoneNumber("223 II")
                .setAddress("Guest St. I")
                .buildEntity();

        repository.save(entity);

        Optional<Guest> result = repository.findByFullName("Guest Non-Existent");
        Optional<Guest> result_2 = repository.findByFullName(null);

        assertTrue(result.isEmpty());
        assertTrue(result_2.isEmpty());
    }
}