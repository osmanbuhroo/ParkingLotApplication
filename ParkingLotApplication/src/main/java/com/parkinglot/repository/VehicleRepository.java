package com.parkinglot.repository;

import com.parkinglot.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);
}