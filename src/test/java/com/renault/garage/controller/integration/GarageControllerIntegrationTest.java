package com.renault.garage.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.dto.garage.GarageCreateDTO;
import com.renault.garage.dto.garage.GarageResponseDTO;
import com.renault.garage.services.GarageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
//@AutoConfigureMockMvc
class GarageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GarageService garageService;

    private GarageCreateDTO createDTO;

    @BeforeEach
    void setUp() {
        createDTO = new GarageCreateDTO();
        createDTO.setName("Garage Int Test");
        createDTO.setAddress("123 Rue Exemple");
        createDTO.setTelephone("0123456789");
        createDTO.setEmail("test@garage.com");
        createDTO.setOpeningTimes(Collections.emptyMap());
    }

    @Test
    void shouldCreateGarage() throws Exception {
        mockMvc.perform(post("/api/garages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Garage Int Test"));
    }

    @Test
    void shouldGetGarageById() throws Exception {
        // Crée un garage via le service
        GarageResponseDTO created = garageService.createGarage(createDTO);

        mockMvc.perform(get("/api/garages/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(created.getId().toString()))
                .andExpect(jsonPath("$.name").value("Garage Int Test"));
    }

    @Test
    void shouldDeleteGarage() throws Exception {
        GarageResponseDTO created = garageService.createGarage(createDTO);

        mockMvc.perform(delete("/api/garages/{id}", created.getId()))
                .andExpect(status().isNoContent());
    }
}
