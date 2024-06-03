package com.parkinglot.repository;

import com.parkinglot.model.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {
    List<ParkingSpace> findByFloorNumberAndVehicleTypeAndIsAvailable(int floorNumber, String vehicleType, boolean isAvailable);
    List<ParkingSpace> findByIsAvailable(boolean isAvailable);
}