package com.parkinglot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String registrationNumber;
    private String color;
    private Long parkingSpaceId;
    private LocalDateTime entryTime;

    public Vehicle() {
        this.entryTime = LocalDateTime.now();
    }

}