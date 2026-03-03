package com.renault.garage.kafka.event;

import java.util.UUID;

public class VehicleCreatedEvent {
    private UUID vehicleId;
    private UUID garageId;
    private String brand;
    private String model;

    public VehicleCreatedEvent() {}

    public VehicleCreatedEvent(UUID vehicleId, UUID garageId, String brand, String model) {
        this.vehicleId = vehicleId;
        this.garageId = garageId;
        this.brand = brand;
        this.model = model;
    }

    public UUID getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(UUID vehicleId) {
        this.vehicleId = vehicleId;
    }

    public UUID getGarageId() {
        return garageId;
    }

    public void setGarageId(UUID garageId) {
        this.garageId = garageId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
