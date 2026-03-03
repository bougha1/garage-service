package com.renault.garage.repository;

import com.renault.garage.entity.Garage;
import com.renault.garage.enums.FuelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface GarageRepository extends JpaRepository<Garage, UUID> {
    @Query("""
        SELECT DISTINCT g
        FROM Garage g
        JOIN g.vehicles v
        WHERE v.fuelType = :fuelType
    """)
    List<Garage> findGaragesByFuelType(FuelType fuelType);

    @Query("""
        SELECT DISTINCT g
        FROM Garage g
        JOIN g.vehicles v
        JOIN v.accessories a
        WHERE a.name = :accessoryName
    """)
    List<Garage> findGaragesByAccessoryName(String accessoryName);
}
