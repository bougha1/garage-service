package com.renault.garage.repository;

import com.renault.garage.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    long countByGarageId(UUID garageId);

    List<Vehicle> findByGarageId(UUID garageId);

    List<Vehicle> findByModel(String model);
}
