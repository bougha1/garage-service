package com.renault.garage.dto.vehicle;

import com.renault.garage.entity.Garage;
import com.renault.garage.enums.FuelType;

import java.util.UUID;

public class VehicleResponseDTO {

    private UUID id;
    private String brand;
    private String model;
    private int manufactureYear;
    private FuelType fuelType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public int getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(int year) {
        this.manufactureYear = year;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

}
