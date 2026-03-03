package com.renault.garage.service.impl;

import com.renault.garage.dto.garage.GarageCreateDTO;
import com.renault.garage.dto.garage.GarageResponseDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.OpeningTime;
import com.renault.garage.enums.FuelType;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.mapper.GarageMapper;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.services.GarageService;
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

    public GarageServiceImpl(GarageRepository garageRepository,
                             GarageMapper garageMapper) {
        this.garageRepository = garageRepository;
        this.garageMapper = garageMapper;
    }

    @Override
    @Transactional
    public GarageResponseDTO createGarage(GarageCreateDTO dto) {

        Garage garage = garageMapper.toEntity(dto);

        List<OpeningTime> times =
                garageMapper.mapOpeningTimes(dto.getOpeningTimes(), garage);

        garage.setOpeningTimes(times);

        Garage saved = garageRepository.save(garage);

        return garageMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public GarageResponseDTO updateGarage(UUID id, GarageCreateDTO dto) {

        Garage garage = garageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Garage not found"));

        garageMapper.updateEntityFromDTO(dto, garage);

        garage.getOpeningTimes().clear();

        List<OpeningTime> openingTimes = garageMapper.mapOpeningTimes(dto.getOpeningTimes(), garage);

        garage.getOpeningTimes().addAll(openingTimes);

        return garageMapper.toResponseDTO(
                garageRepository.save(garage)
        );
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
                .map(garageMapper::toResponseDTO)
                .orElseThrow(() -> new NotFoundException("Garage not found"));
    }

    @Override
    public Page<GarageResponseDTO> getAllGarages(Pageable pageable) {
        return garageRepository.findAll(pageable)
                .map(garageMapper::toResponseDTO);
    }

    @Override
    public List<GarageResponseDTO> searchByFuelType(FuelType fuelType) {
        return garageRepository.findGaragesByFuelType(fuelType)
                .stream()
                .map(garageMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<GarageResponseDTO> searchByAccessory(String accessoryName) {
        return garageRepository.findGaragesByAccessoryName(accessoryName)
                .stream()
                .map(garageMapper::toResponseDTO)
                .toList();
    }
}