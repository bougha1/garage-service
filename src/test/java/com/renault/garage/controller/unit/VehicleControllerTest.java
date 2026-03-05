package com.renault.garage.controller.unit;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.controller.VehicleController;
import com.renault.garage.dto.vehicle.VehicleCreateDTO;
import com.renault.garage.dto.vehicle.VehicleResponseDTO;
import com.renault.garage.enums.FuelType;
import com.renault.garage.services.VehicleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
class VehicleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    VehicleService vehicleService;

    @Test
    void shouldCreateVehicle() throws Exception {
        UUID id = UUID.randomUUID();

        VehicleResponseDTO response = new VehicleResponseDTO();
        response.setId(id);
        response.setBrand("Renault");
        response.setModel("Clio");
        response.setFuelType(FuelType.DIESEL);

        Mockito.when(vehicleService.createVehicle(any())).thenReturn(response);

        VehicleCreateDTO dto = new VehicleCreateDTO();
        dto.setBrand("Renault");
        dto.setModel("Clio");
        dto.setFuelType(FuelType.DIESEL);
        dto.setGarageId(UUID.randomUUID());
        dto.setManufactureYear(2022);

        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("Renault"))
                .andExpect(jsonPath("$.model").value("Clio"));
    }

    @Test
    void shouldGetVehicleById() throws Exception {
        UUID id = UUID.randomUUID();

        VehicleResponseDTO response = new VehicleResponseDTO();
        response.setId(id);
        response.setBrand("Renault");
        response.setModel("Captur");

        Mockito.when(vehicleService.getVehicleById(id)).thenReturn(response);

        mockMvc.perform(get("/api/v1/vehicles/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Captur"));
    }

    @Test
    void shouldReturnVehiclesByFuelType() throws Exception {
        VehicleResponseDTO v = new VehicleResponseDTO();
        v.setId(UUID.randomUUID());
        v.setBrand("Renault");
        v.setModel("Megane");
        v.setFuelType(FuelType.HYBRID);

        Mockito.when(vehicleService.getVehiclesByFuelType(FuelType.HYBRID))
                .thenReturn(List.of(v));

        mockMvc.perform(get("/api/v1/vehicles/by-fuel?fuelType=HYBRID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fuelType").value("HYBRID"));
    }
}

