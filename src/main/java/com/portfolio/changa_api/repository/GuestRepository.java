package com.portfolio.changa_api.repository;

import com.portfolio.changa_api.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    Optional<Guest> findByFullName(String name);
}
