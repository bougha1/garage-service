package com.renault.garage.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.dto.vehicle.VehicleCreateDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.enums.FuelType;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class VehicleControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    GarageRepository garageRepository;

    @Test
    void shouldCreateVehicle() throws Exception {


        Garage garage = new Garage();
        garage.setName("Test Garage");
        garage.setAddress("Rue X");
        garage.setTelephone("0600000000");
        garage.setEmail("garage@test.com");
        garageRepository.saveAndFlush(garage);

        VehicleCreateDTO dto = new VehicleCreateDTO();
        dto.setBrand("Renault");
        dto.setModel("Clio");
        dto.setManufactureYear(2022);
        dto.setFuelType(FuelType.DIESEL);
        dto.setGarageId(garage.getId());

        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("Renault"));
    }

    @Test
    void shouldGetVehicle() throws Exception {

        Vehicle v = new Vehicle();
        v.setBrand("Renault");
        v.setModel("Megane");
        v.setFuelType(FuelType.HYBRID);
        vehicleRepository.save(v);

        mockMvc.perform(get("/api/v1/vehicles/" + v.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Megane"));
    }

    @Test
    void shouldDeleteVehicle() throws Exception {

        Vehicle v = new Vehicle();
        v.setBrand("Renault");
        v.setModel("Kadjar");
        v.setFuelType(FuelType.DIESEL);
        vehicleRepository.save(v);

        mockMvc.perform(delete("/api/v1/vehicles/" + v.getId()))
                .andExpect(status().isNoContent());

        assertTrue(vehicleRepository.findById(v.getId()).isEmpty());
    }
}
