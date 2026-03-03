package com.renault.garage.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.controller.AccessoryController;
import com.renault.garage.dto.accessory.AccessoryCreateDTO;
import com.renault.garage.dto.accessory.AccessoryResponseDTO;
import com.renault.garage.enums.AccessoryType;
import com.renault.garage.services.AccessoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AccessoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccessoryService accessoryService;

    @InjectMocks
    private AccessoryController accessoryController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accessoryController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateAccessory() throws Exception {
        AccessoryCreateDTO createDTO = new AccessoryCreateDTO();
        createDTO.setName("GPS");
        createDTO.setDescription("GPS Navigation");
        createDTO.setPrice(250.0);
        createDTO.setType(AccessoryType.PERFORMANCE);
        createDTO.setVehicleId(UUID.randomUUID());

        AccessoryResponseDTO responseDTO = new AccessoryResponseDTO();
        responseDTO.setId(UUID.randomUUID());
        responseDTO.setName(createDTO.getName());
        responseDTO.setDescription(createDTO.getDescription());
        responseDTO.setPrice(createDTO.getPrice());
        responseDTO.setType(createDTO.getType());

        when(accessoryService.createAccessory(any(AccessoryCreateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/accessories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDTO.getId().toString()))
                .andExpect(jsonPath("$.name").value("GPS"));

        verify(accessoryService, times(1)).createAccessory(any(AccessoryCreateDTO.class));
    }

    @Test
    void shouldGetAccessoryById() throws Exception {
        UUID accessoryId = UUID.randomUUID();
        AccessoryResponseDTO responseDTO = new AccessoryResponseDTO();
        responseDTO.setId(accessoryId);
        responseDTO.setName("GPS");

        when(accessoryService.getAccessoryById(accessoryId)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/accessories/{id}", accessoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accessoryId.toString()))
                .andExpect(jsonPath("$.name").value("GPS"));

        verify(accessoryService, times(1)).getAccessoryById(accessoryId);
    }

    @Test
    void shouldDeleteAccessory() throws Exception {
        UUID accessoryId = UUID.randomUUID();

        doNothing().when(accessoryService).deleteAccessory(accessoryId);

        mockMvc.perform(delete("/api/accessories/{id}", accessoryId))
                .andExpect(status().isNoContent());

        verify(accessoryService, times(1)).deleteAccessory(accessoryId);
    }

    @Test
    void shouldGetAccessoriesByVehicle() throws Exception {
        UUID vehicleId = UUID.randomUUID();
        AccessoryResponseDTO responseDTO = new AccessoryResponseDTO();
        responseDTO.setId(UUID.randomUUID());
        responseDTO.setName("GPS");

        when(accessoryService.getAccessoriesByVehicle(vehicleId)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/accessories/by-vehicle/{vehicleId}", vehicleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("GPS"));

        verify(accessoryService, times(1)).getAccessoriesByVehicle(vehicleId);
    }
}