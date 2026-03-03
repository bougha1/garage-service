package com.renault.garage.services.impl;

import com.renault.garage.dto.accessory.AccessoryCreateDTO;
import com.renault.garage.dto.accessory.AccessoryResponseDTO;
import com.renault.garage.entity.Accessory;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.mapper.AccessoryMapper;
import com.renault.garage.repository.AccessoryRepository;
import com.renault.garage.services.AccessoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccessoryServiceImpl implements AccessoryService {

    private final AccessoryRepository accessoryRepository;
    private final AccessoryMapper accessoryMapper;

    public AccessoryServiceImpl(AccessoryRepository accessoryRepository, AccessoryMapper accessoryMapper) {
        this.accessoryRepository = accessoryRepository;
        this.accessoryMapper = accessoryMapper;
    }

    @Override
    public AccessoryResponseDTO getAccessoryById(UUID id) {
        Accessory accessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Accessory not found"));
        return accessoryMapper.toResponseDTO(accessory);
    }

    @Override
    public AccessoryResponseDTO createAccessory(AccessoryCreateDTO dto) {
        Accessory accessory = accessoryMapper.toEntity(dto);
        return accessoryMapper.toResponseDTO(accessoryRepository.save(accessory));
    }

    @Override
    public AccessoryResponseDTO updateAccessory(UUID id, AccessoryCreateDTO dto) {
        Accessory accessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Accessory not found"));
        accessoryMapper.updateEntityFromDTO(dto, accessory);
        return accessoryMapper.toResponseDTO(accessoryRepository.save(accessory));
    }

    @Override
    public void deleteAccessory(UUID id) {
        Accessory accessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Accessory not found"));
        accessoryRepository.delete(accessory);
    }

    @Override
    public List<AccessoryResponseDTO> getAccessoriesByVehicle(UUID vehicleId) {
        return accessoryRepository.findByVehicleId(vehicleId)
                .stream()
                .map(accessoryMapper::toResponseDTO)
                .toList();
    }
}