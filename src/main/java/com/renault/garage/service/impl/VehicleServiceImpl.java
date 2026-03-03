package com.renault.garage.service.impl;

import com.renault.garage.dto.vehicle.VehicleCreateDTO;
import com.renault.garage.dto.vehicle.VehicleResponseDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.exception.QuotaExceededException;
import com.renault.garage.kafka.event.VehicleCreatedEvent;
import com.renault.garage.kafka.producer.VehicleProducer;
import com.renault.garage.mapper.VehicleMapper;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import com.renault.garage.services.VehicleService;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final GarageRepository garageRepository;
    private final VehicleProducer vehicleProducer;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper, GarageRepository garageRepository, VehicleProducer vehicleProducer) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.garageRepository = garageRepository;
        this.vehicleProducer = vehicleProducer;
    }

    @Override
    public VehicleResponseDTO getVehicleById(UUID id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));
        return vehicleMapper.toResponseDTO(vehicle);
    }

    @Override
    @Transactional
    public VehicleResponseDTO createVehicle(VehicleCreateDTO dto) {
        //Vérifier le quota, garage, etc.
        Garage garage = garageRepository.findById(dto.getGarageId())
                .orElseThrow(() -> new NotFoundException("Garage not found"));

        if (garage.getVehicles().size() >= 50) {
            throw new QuotaExceededException("Garage quota exceeded");
        }

        Vehicle vehicle = vehicleMapper.toEntity(dto);
        vehicle.setGarage(garage);
        vehicleRepository.save(vehicle);

        //Publier l'événement
        VehicleCreatedEvent event = new VehicleCreatedEvent(
                vehicle.getId(),
                garage.getId(),
                vehicle.getBrand(),
                vehicle.getModel()
        );
        vehicleProducer.sendVehicleCreatedEvent(event);

        return vehicleMapper.toResponseDTO(vehicle);
    }

    @Override
    @Transactional
    public VehicleResponseDTO updateVehicle(UUID id, VehicleCreateDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));

        UUID currentGarageId = vehicle.getGarage().getId();
        UUID targetGarageId = dto.getGarageId();

        if (!currentGarageId.equals(targetGarageId)) {
            Garage newGarage = checkQuotaAndGetGarage(targetGarageId);
            vehicle.setGarage(newGarage);
        }

        vehicleMapper.updateEntityFromDTO(dto, vehicle);

        return vehicleMapper.toResponseDTO(vehicleRepository.save(vehicle));
    }

    @Override
    @Transactional
    public void deleteVehicle(UUID id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));
        vehicleRepository.delete(vehicle);
    }

    @Override
    public List<VehicleResponseDTO> getVehiclesByGarage(UUID garageId) {
        return vehicleRepository.findByGarageId(garageId)
                .stream()
                .map(vehicleMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<VehicleResponseDTO> getVehiclesByModel(String model) {
        return vehicleRepository.findByModel(model)
                .stream()
                .map(vehicleMapper::toResponseDTO)
                .toList();
    }

    private Garage checkQuotaAndGetGarage(UUID garageId) {

        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new NotFoundException("Garage not found"));

        long vehicleCount = vehicleRepository.countByGarageId(garageId);

        if (vehicleCount >= 50) {
            throw new QuotaExceededException(
                    "Garage " + garageId + " has reached the maximum of 50 vehicles"
            );
        }

        return garage;
    }
}