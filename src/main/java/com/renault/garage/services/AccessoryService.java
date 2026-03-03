package com.renault.garage.services;

import com.renault.garage.dto.accessory.AccessoryCreateDTO;
import com.renault.garage.dto.accessory.AccessoryResponseDTO;
import java.util.List;
import java.util.UUID;

public interface AccessoryService {

    AccessoryResponseDTO createAccessory(AccessoryCreateDTO dto);

    AccessoryResponseDTO updateAccessory(UUID id, AccessoryCreateDTO dto);

    AccessoryResponseDTO getAccessoryById(UUID id);

    void deleteAccessory(UUID id);

    List<AccessoryResponseDTO> getAccessoriesByVehicle(UUID vehicleId);
}