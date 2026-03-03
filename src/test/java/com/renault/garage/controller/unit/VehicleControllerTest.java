package com.renault.garage.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.controller.VehicleController;
import com.renault.garage.dto.vehicle.VehicleCreateDTO;
import com.renault.garage.dto.vehicle.VehicleResponseDTO;
import com.renault.garage.enums.FuelType;
import com.renault.garage.services.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VehicleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private VehicleController vehicleController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateVehicle() throws Exception {
        VehicleCreateDTO createDTO = new VehicleCreateDTO();
        createDTO.setBrand("Renault");
        createDTO.setModel("Clio");
        createDTO.setFuelType(FuelType.HYBRID);
        createDTO.setGarageId(UUID.randomUUID());

        VehicleResponseDTO responseDTO = new VehicleResponseDTO();
        responseDTO.setId(UUID.randomUUID());
        responseDTO.setBrand(createDTO.getBrand());
        responseDTO.setModel(createDTO.getModel());

        when(vehicleService.createVehicle(any(VehicleCreateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated());
        verify(vehicleService, times(1)).createVehicle(any(VehicleCreateDTO.class));
    }

    @Test
    void shouldUpdateVehicle() throws Exception {
        UUID vehicleId = UUID.randomUUID();
        VehicleCreateDTO updateDTO = new VehicleCreateDTO();
        updateDTO.setBrand("Renault");
        updateDTO.setModel("Megane");
        updateDTO.setFuelType(FuelType.HYBRID);
        updateDTO.setGarageId(UUID.randomUUID());

        VehicleResponseDTO responseDTO = new VehicleResponseDTO();
        responseDTO.setId(vehicleId);
        responseDTO.setBrand(updateDTO.getBrand());

        when(vehicleService.updateVehicle(eq(vehicleId), any(VehicleCreateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/vehicles/{id}", vehicleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vehicleId.toString()))
                .andExpect(jsonPath("$.brand").value("Renault"));

        verify(vehicleService, times(1)).updateVehicle(eq(vehicleId), any(VehicleCreateDTO.class));
    }

    @Test
    void shouldDeleteVehicle() throws Exception {
        UUID vehicleId = UUID.randomUUID();

        doNothing().when(vehicleService).deleteVehicle(vehicleId);

        mockMvc.perform(delete("/api/vehicles/{id}", vehicleId))
                .andExpect(status().isNoContent());

        verify(vehicleService, times(1)).deleteVehicle(vehicleId);
    }

    @Test
    void shouldGetVehicleById() throws Exception {
        UUID vehicleId = UUID.randomUUID();
        VehicleResponseDTO responseDTO = new VehicleResponseDTO();
        responseDTO.setId(vehicleId);
        responseDTO.setBrand("Renault");
        responseDTO.setModel("Clio");

        when(vehicleService.getVehicleById(vehicleId)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/vehicles/{id}", vehicleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vehicleId.toString()))
                .andExpect(jsonPath("$.brand").value("Renault"))
                .andExpect(jsonPath("$.model").value("Clio"));

        verify(vehicleService, times(1)).getVehicleById(vehicleId);
    }

    @Test
    void shouldGetVehiclesByGarage() throws Exception {
        UUID garageId = UUID.randomUUID();
        VehicleResponseDTO vehicleDTO = new VehicleResponseDTO();
        vehicleDTO.setId(UUID.randomUUID());
        vehicleDTO.setBrand("Renault");

        when(vehicleService.getVehiclesByGarage(garageId)).thenReturn(List.of(vehicleDTO));

        mockMvc.perform(get("/api/vehicles/by-garage/{garageId}", garageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value("Renault"));

        verify(vehicleService, times(1)).getVehiclesByGarage(garageId);
    }

    @Test
    void shouldGetVehiclesByModel() throws Exception {
        String model = "Clio";
        VehicleResponseDTO vehicleDTO = new VehicleResponseDTO();
        vehicleDTO.setId(UUID.randomUUID());
        vehicleDTO.setModel(model);

        when(vehicleService.getVehiclesByModel(model)).thenReturn(List.of(vehicleDTO));

        mockMvc.perform(get("/api/vehicles/by-model")
                        .param("model", model))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].model").value("Clio"));

        verify(vehicleService, times(1)).getVehiclesByModel(model);
    }
}
