package com.renault.garage.service.impl;

import com.renault.garage.dto.garage.GarageCreateDTO;
import com.renault.garage.dto.garage.GarageResponseDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.enums.FuelType;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.mapper.GarageMapper;
import com.renault.garage.mapper.OpeningTimeMapper;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.OpeningTimeRepository;
import com.renault.garage.services.GarageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GarageServiceImpl implements GarageService {

    private final GarageRepository garageRepository;
    private final GarageMapper garageMapper;
    private final OpeningTimeMapper openingTimeMapper;

    public GarageServiceImpl(GarageRepository garageRepository,
                             GarageMapper garageMapper, OpeningTimeRepository openingTimeRepository, OpeningTimeMapper openingTimeMapper) {
        this.garageRepository = garageRepository;
        this.garageMapper = garageMapper;
        this.openingTimeMapper = openingTimeMapper;
    }

    @Transactional
    public GarageResponseDTO createGarage(GarageCreateDTO dto) {

        Garage garage = garageMapper.toEntity(dto, openingTimeMapper);
        Garage saved = garageRepository.save(garage);
        return garageMapper.toResponseDTO(saved, openingTimeMapper);

    }

    @Transactional
    public GarageResponseDTO updateGarage(UUID id, GarageCreateDTO dto) {

        Garage entity = garageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Garage not found: " + id));

        garageMapper.updateEntityFromDTO(dto, entity);
        garageMapper.replaceOpeningTimes(entity, dto.getOpeningTimes(), openingTimeMapper);

        Garage saved = garageRepository.save(entity);
        return garageMapper.toResponseDTO(saved, openingTimeMapper);

    }

    @Override
    public void deleteGarage(UUID id) {
        if (!garageRepository.existsById(id)) {
            throw new NotFoundException("Garage not found");
        }
        garageRepository.deleteById(id);

    }

    @Override
    public GarageResponseDTO getGarageById(UUID id) {
        return garageRepository.findById(id)
                .map(garage -> garageMapper.toResponseDTO(garage, openingTimeMapper))
                .orElseThrow(() -> new NotFoundException("Garage not found"));

    }

    @Override
    public Page<GarageResponseDTO> getAllGarages(Pageable pageable) {
        return garageRepository.findAll(pageable)
                .map(garage -> garageMapper.toResponseDTO(garage, openingTimeMapper));

    }

    @Override
    public List<GarageResponseDTO> searchByFuelType(FuelType fuelType) {
        return garageRepository.findGaragesByFuelType(fuelType)
                .stream()
                .map(garage -> garageMapper.toResponseDTO(garage, openingTimeMapper))
                .toList();

    }

    @Override
    public List<GarageResponseDTO> searchByAccessory(String accessoryName) {
        return garageRepository.findGaragesByAccessoryName(accessoryName)
                .stream()
                .map(garage -> garageMapper.toResponseDTO(garage, openingTimeMapper))
                .toList();

    }
}