package com.renault.garage.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.dto.accessory.AccessoryCreateDTO;
import com.renault.garage.entity.Accessory;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.enums.AccessoryType;
import com.renault.garage.enums.FuelType;
import com.renault.garage.repository.AccessoryRepository;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccessoryControllerIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper mapper;
    @Autowired GarageRepository garageRepository;
    @Autowired VehicleRepository vehicleRepository;
    @Autowired AccessoryRepository accessoryRepository;


    @Test
    void shouldCreateAccessory() throws Exception {

        Garage garage = new Garage();
        garage.setName("Test Garage");
        garage.setAddress("Rue X");
        garage.setEmail("garage@test.com");
        garage.setTelephone("0600000000");
        garageRepository.saveAndFlush(garage);

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("Renault");
        vehicle.setModel("Clio");
        vehicle.setFuelType(FuelType.DIESEL);
        vehicle.setManufactureYear(2020);
        vehicle.setGarage(garage);
        vehicle = vehicleRepository.saveAndFlush(vehicle);

        AccessoryCreateDTO dto = new AccessoryCreateDTO();
        dto.setName("Roof Rack");
        dto.setDescription("Steel rack");
        dto.setPrice(BigDecimal.valueOf(200));
        dto.setType(AccessoryType.COMFORT);
        dto.setVehicleId(vehicle.getId());

        mockMvc.perform(post("/api/v1/accessories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Roof Rack"))
                .andExpect(jsonPath("$.vehicleId").value(vehicle.getId().toString()));
    }


    @Test
    void shouldGetAccessory() throws Exception {

        Garage garage = new Garage();
        garage.setName("Test Garage");
        garage.setAddress("Rue X");
        garage.setEmail("garage@test.com");
        garage.setTelephone("0600000000");
        garageRepository.saveAndFlush(garage);

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("Peugeot");
        vehicle.setModel("208");
        vehicle.setFuelType(FuelType.DIESEL);
        vehicle.setManufactureYear(2021);
        vehicle.setGarage(garage);
        vehicle = vehicleRepository.saveAndFlush(vehicle);

        Accessory a = new Accessory();
        a.setName("GPS");
        a.setDescription("TomTom");
        a.setPrice(BigDecimal.valueOf(150));
        a.setType(AccessoryType.MULTIMEDIA);
        a.setVehicle(vehicle);

        accessoryRepository.saveAndFlush(a);

        mockMvc.perform(get("/api/v1/accessories/" + a.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("GPS"));
    }

    @Test
    void shouldDeleteAccessory() throws Exception {

        Garage garage = new Garage();
        garage.setName("Test Garage");
        garage.setAddress("Rue X");
        garage.setEmail("garage@test.com");
        garage.setTelephone("0600000000");
        garageRepository.saveAndFlush(garage);

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("Dacia");
        vehicle.setModel("Duster");
        vehicle.setFuelType(FuelType.DIESEL);
        vehicle.setManufactureYear(2022);
        vehicle.setGarage(garage);
        vehicle = vehicleRepository.saveAndFlush(vehicle);

        Accessory a = new Accessory();
        a.setName("Floor Mat");
        a.setPrice(BigDecimal.valueOf(30));
        a.setType(AccessoryType.PERFORMANCE);
        a.setVehicle(vehicle);

        accessoryRepository.saveAndFlush(a);

        mockMvc.perform(delete("/api/v1/accessories/" + a.getId()))
                .andExpect(status().isNoContent());

        assertTrue(accessoryRepository.findById(a.getId()).isEmpty());
    }
}