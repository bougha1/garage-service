package com.renault.garage.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.controller.AccessoryController;
import com.renault.garage.dto.accessory.AccessoryCreateDTO;
import com.renault.garage.dto.accessory.AccessoryResponseDTO;
import com.renault.garage.enums.AccessoryType;
import com.renault.garage.services.AccessoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccessoryController.class)
class AccessoryControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper mapper;
    @MockitoBean AccessoryService accessoryService;

    @Test
    void shouldCreateAccessory() throws Exception {
        UUID id = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();

        AccessoryResponseDTO response = new AccessoryResponseDTO();
        response.setId(id);
        response.setName("Roof Rack");
        response.setDescription("Black steel");
        response.setPrice(BigDecimal.valueOf(199.99));
        response.setType(AccessoryType.COMFORT);
        response.setVehicleId(vehicleId);

        when(accessoryService.createAccessory(any())).thenReturn(response);

        AccessoryCreateDTO dto = new AccessoryCreateDTO();
        dto.setName("Roof Rack");
        dto.setDescription("Black steel");
        dto.setPrice(BigDecimal.valueOf(199.99));
        dto.setType(AccessoryType.COMFORT);
        dto.setVehicleId(vehicleId);

        mockMvc.perform(post("/api/v1/accessories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Roof Rack"))
                .andExpect(jsonPath("$.price").value(199.99));
    }

    @Test
    void shouldGetAccessoryById() throws Exception {
        UUID id = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();

        AccessoryResponseDTO dto = new AccessoryResponseDTO();
        dto.setId(id);
        dto.setName("GPS");
        dto.setPrice(BigDecimal.valueOf(120));
        dto.setType(AccessoryType.MULTIMEDIA);
        dto.setVehicleId(vehicleId);

        when(accessoryService.getAccessoryById(id)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/accessories/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("GPS"));
    }

    @Test
    void shouldGetByVehicleId() throws Exception {
        UUID vehicleId = UUID.randomUUID();

        AccessoryResponseDTO dto = new AccessoryResponseDTO();
        dto.setId(UUID.randomUUID());
        dto.setName("Seat Cover");
        dto.setType(AccessoryType.SECURITY);
        dto.setVehicleId(vehicleId);
        dto.setPrice(BigDecimal.valueOf(50));

        when(accessoryService.getAccessoriesByVehicle(vehicleId))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/accessories/by-vehicle/" + vehicleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Seat Cover"));
    }
}
