package com.renault.garage.services;

import com.renault.garage.dto.garage.GarageCreateDTO;
import com.renault.garage.dto.garage.GarageResponseDTO;
import com.renault.garage.enums.FuelType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface GarageService {

    GarageResponseDTO createGarage(GarageCreateDTO dto);

    GarageResponseDTO updateGarage(UUID id, GarageCreateDTO dto);

    void deleteGarage(UUID id);

    GarageResponseDTO getGarageById(UUID id);

    Page<GarageResponseDTO> getAllGarages(Pageable pageable);

    List<GarageResponseDTO> searchByFuelType(FuelType fuelType);

    List<GarageResponseDTO> searchByAccessory(String accessoryName);
}