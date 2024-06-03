package com.parkinglot.controller;

import com.parkinglot.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {
    @Autowired
    private ParkingService parkingService;

    @PostMapping("/initialize")
    public void initializeParkingLot(@RequestBody Map<String, Integer> request) {
        int floors = request.get("floors");
        int spacesPerFloor = request.get("spacesPerFloor");
        parkingService.initializeParkingLot(floors, spacesPerFloor);
    }

    @PostMapping("/addVehicle")
    public String addVehicle(@RequestBody Map<String, String> request) {
        String type = request.get("type");
        String registrationNumber = request.get("registrationNumber");
        String color = request.get("color");
        return parkingService.addVehicle(type, registrationNumber, color);
    }

    @DeleteMapping("/removeVehicle")
    public String removeVehicle(@RequestBody Map<String, String> request) {
        String registrationNumber = request.get("registrationNumber");
        LocalDateTime exitTime = LocalDateTime.parse(request.get("exitTime"));
        return parkingService.removeVehicle(registrationNumber, exitTime);
    }

    @GetMapping("/checkAvailability")
    public boolean checkAvailability(@RequestParam int floor, @RequestParam String vehicleType) {
        return parkingService.checkAvailability(floor, vehicleType);
    }
}
