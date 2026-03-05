package com.renault.garage.service.integration;

import com.renault.garage.dto.vehicle.VehicleCreateDTO;
import com.renault.garage.dto.vehicle.VehicleResponseDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.enums.FuelType;
import com.renault.garage.kafka.producer.VehicleProducer;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import com.renault.garage.services.VehicleService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class VehicleServiceIntegration {


    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private GarageRepository garageRepository;
    @MockitoBean
    private VehicleProducer vehicleProducer;


    private Garage createGarage() {
        Garage g = new Garage();
        g.setName("Orig Garage");
        g.setAddress("123 rue");
        g.setTelephone("0678954326");
        g.setEmail("orig@test.com");
        return garageRepository.save(g);
    }

    private VehicleCreateDTO createVehicleDTO(UUID garageId) {
        VehicleCreateDTO dto = new VehicleCreateDTO();
        dto.setBrand("Renault");
        dto.setModel("Clio");
        dto.setFuelType(FuelType.DIESEL);
        dto.setManufactureYear(2020);
        dto.setGarageId(garageId);
        return dto;
    }

    @Test
    void shouldCreateVehicle() {
        Garage garage = createGarage();
        VehicleCreateDTO dto = createVehicleDTO(garage.getId());

        doNothing().when(vehicleProducer).sendVehicleCreatedEvent(any());
        VehicleResponseDTO response = vehicleService.createVehicle(dto);

        assertNotNull(response.getId());
        assertEquals("Renault", response.getBrand());

        Vehicle saved = vehicleRepository.findById(response.getId()).orElseThrow();
        assertEquals("Clio", saved.getModel());
    }

    @Test
    void shouldGetVehicleById() {
        Garage g = createGarage();

        Vehicle v = new Vehicle();
        v.setBrand("Renault");
        v.setModel("Megane");
        v.setFuelType(FuelType.DIESEL);
        v.setManufactureYear(2022);
        v.setGarage(g);
        vehicleRepository.save(v);

        VehicleResponseDTO loaded = vehicleService.getVehicleById(v.getId());

        assertEquals(v.getId(), loaded.getId());
        assertEquals("Megane", loaded.getModel());
    }

    @Test
    void shouldUpdateVehicle() {
        Garage g = createGarage();

        // existing vehicle
        Vehicle v = new Vehicle();
        v.setBrand("Renault");
        v.setModel("Clio");
        v.setFuelType(FuelType.ESSENCE);
        v.setManufactureYear(2019);
        v.setGarage(g);
        vehicleRepository.save(v);

        // update dto
        VehicleCreateDTO dto = new VehicleCreateDTO();
        dto.setBrand("Dacia");
        dto.setModel("Duster");
        dto.setFuelType(FuelType.DIESEL);
        dto.setManufactureYear(2023);
        dto.setGarageId(g.getId());

        VehicleResponseDTO updated = vehicleService.updateVehicle(v.getId(), dto);

        assertEquals("Dacia", updated.getBrand());
        assertEquals("Duster", updated.getModel());

        Vehicle saved = vehicleRepository.findById(v.getId()).orElseThrow();
        assertEquals(FuelType.DIESEL, saved.getFuelType());
    }

    @Test
    void shouldDeleteVehicle() {
        Garage g = createGarage();

        Vehicle v = new Vehicle();
        v.setBrand("Peugeot");
        v.setModel("208");
        v.setFuelType(FuelType.ELECTRIC);
        v.setManufactureYear(2021);
        v.setGarage(g);
        vehicleRepository.save(v);

        vehicleService.deleteVehicle(v.getId());

        assertFalse(vehicleRepository.findById(v.getId()).isPresent());
    }

    @Test
    void shouldGetVehiclesByGarage() {
        Garage g = createGarage();

        Vehicle v1 = new Vehicle();
        v1.setBrand("Renault");
        v1.setModel("Clio");
        v1.setFuelType(FuelType.DIESEL);
        v1.setManufactureYear(2018);
        v1.setGarage(g);
        vehicleRepository.save(v1);

        Vehicle v2 = new Vehicle();
        v2.setBrand("Renault");
        v2.setModel("Megane");
        v2.setFuelType(FuelType.HYBRID);
        v2.setManufactureYear(2025);
        v2.setGarage(g);
        vehicleRepository.save(v2);

        List<VehicleResponseDTO> list = vehicleService.getVehiclesByGarage(g.getId());

        assertEquals(2, list.size());
    }

    @Test
    void shouldGetAllVehicles() {
        Garage g = createGarage();

        Vehicle v1 = new Vehicle();
        v1.setBrand("BMW");
        v1.setModel("X1");
        v1.setFuelType(FuelType.DIESEL);
        v1.setManufactureYear(2017);
        v1.setGarage(g);
        vehicleRepository.save(v1);

        Vehicle v2 = new Vehicle();
        v2.setBrand("Audi");
        v2.setModel("A3");
        v2.setFuelType(FuelType.DIESEL);
        v2.setManufactureYear(2019);
        v2.setGarage(g);
        vehicleRepository.save(v2);

        List<VehicleResponseDTO> list = vehicleService.getAllVehicles();

        assertEquals(2, list.size());
    }

    @Test
    void shouldGetVehiclesByFuelType() {
        Garage g = createGarage();

        Vehicle v1 = new Vehicle();
        v1.setBrand("Tesla");
        v1.setModel("Model 3");
        v1.setFuelType(FuelType.ELECTRIC);
        v1.setManufactureYear(2021);
        v1.setGarage(g);
        vehicleRepository.save(v1);

        Vehicle v2 = new Vehicle();
        v2.setBrand("Renault");
        v2.setModel("Clio");
        v2.setFuelType(FuelType.DIESEL);
        v2.setManufactureYear(2020);
        v2.setGarage(g);
        vehicleRepository.save(v2);

        List<VehicleResponseDTO> list = vehicleService.getVehiclesByFuelType(FuelType.ELECTRIC);

        assertEquals(1, list.size());
        assertEquals("Tesla", list.get(0).getBrand());
    }

}
