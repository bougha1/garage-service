package com.renault.garage.repository;

import com.renault.garage.entity.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccessoryRepository extends JpaRepository<Accessory, UUID> {

    List<Accessory> findByVehicleId(UUID vehicleId);
}
