package com.renault.garage.dto.accessory;

import com.renault.garage.enums.AccessoryType;

import java.math.BigDecimal;
import java.util.UUID;

public class AccessoryResponseDTO {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private UUID vehicleId;
    private AccessoryType type;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public AccessoryType getType() {
        return type;
    }

    public void setType(AccessoryType type) {
        this.type = type;
    }

    public UUID getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(UUID vehicleId) {
        this.vehicleId = vehicleId;
    }
}
