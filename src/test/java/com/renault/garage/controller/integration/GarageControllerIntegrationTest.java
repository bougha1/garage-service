package com.renault.garage.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.dto.garage.GarageCreateDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.repository.GarageRepository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GarageControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    GarageRepository garageRepository;

    @Test
    void createGarage_success() throws Exception {
        GarageCreateDTO dto = new GarageCreateDTO();
        dto.setName("Garage Integration");
        dto.setAddress("Rue Test");
        dto.setTelephone("0700000000");
        dto.setEmail("test@garage.com");
        dto.setOpeningTimes(Collections.emptyMap());

        mockMvc.perform(post("/api/v1/garages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Garage Integration"));
    }

    @Test
    void getGarage_success() throws Exception {
        Garage g = new Garage();
        g.setName("Garage G1");
        g.setAddress("A1");
        g.setTelephone("0606060606");
        g.setEmail("g1@test.com");
        garageRepository.save(g);

        mockMvc.perform(get("/api/v1/garages/" + g.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Garage G1"));
    }

    @Test
    void deleteGarage_success() throws Exception {
        Garage g = new Garage();
        g.setName("Delete Me");
        g.setAddress("X");
        g.setTelephone("0600000000");
        g.setEmail("d@test.com");
        garageRepository.save(g);

        mockMvc.perform(delete("/api/v1/garages/" + g.getId()))
                .andExpect(status().isNoContent());

        assertTrue(garageRepository.findById(g.getId()).isEmpty());
    }
}
