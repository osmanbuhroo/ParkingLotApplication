package com.parkinglot.service;

import com.parkinglot.model.ParkingSpace;
import com.parkinglot.model.Vehicle;
import com.parkinglot.repository.ParkingSpaceRepository;
import com.parkinglot.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ParkingService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    private static final int CAR_RATE = 20;
    private static final int BIKE_RATE = 10;
    private static final int BUS_RATE = 30;

    @Transactional
    public void initializeParkingLot(int floors, int spacesPerFloor) {
        for (int i = 1; i <= floors; i++) {
            for (int j = 1; j <= spacesPerFloor; j++) {
                ParkingSpace carSpace = new ParkingSpace();
                carSpace.setFloorNumber(i);
                carSpace.setVehicleType("Car");
                carSpace.setAvailable(true);
                parkingSpaceRepository.save(carSpace);

                ParkingSpace bikeSpace = new ParkingSpace();
                bikeSpace.setFloorNumber(i);
                bikeSpace.setVehicleType("Bike");
                bikeSpace.setAvailable(true);
                parkingSpaceRepository.save(bikeSpace);

                ParkingSpace busSpace = new ParkingSpace();
                busSpace.setFloorNumber(i);
                busSpace.setVehicleType("Bus");
                busSpace.setAvailable(true);
                parkingSpaceRepository.save(busSpace);
            }
        }
    }

    public String addVehicle(String type, String registrationNumber, String color) {
        Optional<ParkingSpace> availableSpace = parkingSpaceRepository.findByIsAvailable(true).stream()
                .filter(space -> space.getVehicleType().equals(type))
                .findFirst();

        if (availableSpace.isPresent()) {
            Vehicle vehicle = new Vehicle();
            vehicle.setType(type);
            vehicle.setRegistrationNumber(registrationNumber);
            vehicle.setColor(color);
            vehicle.setParkingSpaceId(availableSpace.get().getId());

            availableSpace.get().setAvailable(false);
            parkingSpaceRepository.save(availableSpace.get());
            vehicleRepository.save(vehicle);

            return "Vehicle added with registration number: " + registrationNumber;
        } else {
            return "No available space for vehicle type: " + type;
        }
    }

    public String removeVehicle(String registrationNumber, LocalDateTime exitTime) {
        Optional<Vehicle> vehicleOpt = vehicleRepository.findByRegistrationNumber(registrationNumber);
        if (vehicleOpt.isPresent()) {
            Vehicle vehicle = vehicleOpt.get();
            Optional<ParkingSpace> parkingSpaceOpt = parkingSpaceRepository.findById(vehicle.getParkingSpaceId());

            if (parkingSpaceOpt.isPresent()) {
                parkingSpaceOpt.get().setAvailable(true);
                parkingSpaceRepository.save(parkingSpaceOpt.get());
            }

            long hoursParked = Duration.between(vehicle.getEntryTime(), exitTime).toHours();
            if (hoursParked <= 0) {
                return "Invalid time, cannot calculate cost.";
            }

            int rate = getRate(vehicle.getType());
            int totalCost = (int) hoursParked * rate;

            vehicleRepository.delete(vehicle);

            return "Vehicle with registration number " + registrationNumber + " removed. Total cost: ₹" + totalCost;
        } else {
            return "Vehicle with registration number " + registrationNumber + " not found";
        }
    }

    public boolean checkAvailability(int floor, String vehicleType) {
        return parkingSpaceRepository.findByFloorNumberAndVehicleTypeAndIsAvailable(floor, vehicleType, true).size() > 0;
    }

    private int getRate(String vehicleType) {
        switch (vehicleType) {
            case "Car":
                return CAR_RATE;
            case "Bike":
                return BIKE_RATE;
            case "Bus":
                return BUS_RATE;
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
        }
    }
}