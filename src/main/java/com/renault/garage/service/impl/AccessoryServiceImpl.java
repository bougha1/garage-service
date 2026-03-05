package com.renault.garage.service.impl;

import com.renault.garage.dto.accessory.AccessoryCreateDTO;
import com.renault.garage.dto.accessory.AccessoryResponseDTO;
import com.renault.garage.entity.Accessory;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.mapper.AccessoryMapper;
import com.renault.garage.repository.AccessoryRepository;
import com.renault.garage.repository.VehicleRepository;
import com.renault.garage.services.AccessoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccessoryServiceImpl implements AccessoryService {

    private final AccessoryRepository accessoryRepository;
    private final AccessoryMapper accessoryMapper;
    private final VehicleRepository vehicleRepository;

    public AccessoryServiceImpl(AccessoryRepository accessoryRepository, AccessoryMapper accessoryMapper, VehicleRepository vehicleRepository) {
        this.accessoryRepository = accessoryRepository;
        this.accessoryMapper = accessoryMapper;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public AccessoryResponseDTO createAccessory(AccessoryCreateDTO dto) {

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));

        Accessory accessory = accessoryMapper.toEntity(dto);
        accessory.setVehicle(vehicle);

        Accessory saved = accessoryRepository.save(accessory);

        return accessoryMapper.toResponseDTO(saved);
    }


    @Override
    public AccessoryResponseDTO updateAccessory(UUID id, AccessoryCreateDTO dto) {

        Accessory existing = accessoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Accessory not found"));

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));

        accessoryMapper.updateEntityFromDTO(dto, existing);
        existing.setVehicle(vehicle);

        Accessory saved = accessoryRepository.save(existing);
        return accessoryMapper.toResponseDTO(saved);
    }

    @Override
    public AccessoryResponseDTO getAccessoryById(UUID id) {
        Accessory accessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Accessory not found"));

        return accessoryMapper.toResponseDTO(accessory);
    }

    @Override
    public void deleteAccessory(UUID id) {

        if(!accessoryRepository.existsById(id)) {
            throw new NotFoundException("Accessory not found");
        }
        accessoryRepository.deleteById(id);
    }

    @Override
    public List<AccessoryResponseDTO> getAccessoriesByVehicle(UUID vehicleId) {

        return accessoryRepository.findByVehicleId(vehicleId)
                .stream()
                .map(accessoryMapper::toResponseDTO)
                .toList();
    }

}