package com.renault.garage.services;

import com.renault.garage.dto.vehicle.VehicleCreateDTO;
import com.renault.garage.dto.vehicle.VehicleResponseDTO;
import com.renault.garage.enums.FuelType;

import java.util.List;
import java.util.UUID;

public interface VehicleService {

    VehicleResponseDTO createVehicle(VehicleCreateDTO dto);

    VehicleResponseDTO updateVehicle(UUID id, VehicleCreateDTO dto);

    VehicleResponseDTO getVehicleById(UUID id);

    void deleteVehicle(UUID id);

    List<VehicleResponseDTO> getVehiclesByGarage(UUID garageId);

    List<VehicleResponseDTO> getVehiclesByModel(String model);

    List<VehicleResponseDTO> getAllVehicles();

    List<VehicleResponseDTO> getVehiclesByFuelType(FuelType fuelType);
}