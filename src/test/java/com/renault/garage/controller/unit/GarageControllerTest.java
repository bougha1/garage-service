package com.renault.garage.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.controller.GarageController;
import com.renault.garage.dto.garage.GarageCreateDTO;
import com.renault.garage.dto.garage.GarageResponseDTO;
import com.renault.garage.enums.FuelType;
import com.renault.garage.services.GarageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GarageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GarageService garageService;

    @InjectMocks
    private GarageController garageController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(garageController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateGarage() throws Exception {
        GarageCreateDTO createDTO = new GarageCreateDTO();
        createDTO.setName("Garage Test");
        createDTO.setAddress("123 Rue Exemple");
        createDTO.setTelephone("0123456789");
        createDTO.setEmail("test@garage.com");
        createDTO.setOpeningTimes(Collections.emptyMap());

        GarageResponseDTO responseDTO = new GarageResponseDTO();
        responseDTO.setId(UUID.randomUUID());
        responseDTO.setName(createDTO.getName());
        responseDTO.setAddress(createDTO.getAddress());
        responseDTO.setTelephone(createDTO.getTelephone());
        responseDTO.setEmail(createDTO.getEmail());

        when(garageService.createGarage(any(GarageCreateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/garages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated());

        verify(garageService, times(1)).createGarage(any(GarageCreateDTO.class));
    }

    @Test
    void shouldGetGarageById() throws Exception {
        UUID garageId = UUID.randomUUID();
        GarageResponseDTO responseDTO = new GarageResponseDTO();
        responseDTO.setId(garageId);
        responseDTO.setName("Garage Test");

        when(garageService.getGarageById(garageId)).thenReturn(responseDTO);

        mockMvc.perform(get("/api//garages/{id}", garageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(garageId.toString()))
                .andExpect(jsonPath("$.name").value("Garage Test"));

        verify(garageService, times(1)).getGarageById(garageId);
    }

    @Test
    void shouldDeleteGarage() throws Exception {
        UUID garageId = UUID.randomUUID();

        doNothing().when(garageService).deleteGarage(garageId);

        mockMvc.perform(delete("/api/garages/{id}", garageId))
                .andExpect(status().isNoContent());

        verify(garageService, times(1)).deleteGarage(garageId);
    }

    @Test
    void shouldGetAllGarages() throws Exception {
        GarageResponseDTO garageDTO = new GarageResponseDTO();
        garageDTO.setId(UUID.randomUUID());
        garageDTO.setName("Garage 1");

        Page<GarageResponseDTO> page = new PageImpl<>(List.of(garageDTO));
        when(garageService.getAllGarages(any())).thenReturn(page);

        mockMvc.perform(get("/api/garages")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Garage 1"));
    }

    @Test
    void shouldSearchByFuelType() throws Exception {
        GarageResponseDTO garageDTO = new GarageResponseDTO();
        garageDTO.setId(UUID.randomUUID());
        garageDTO.setName("Garage Fuel");

        when(garageService.searchByFuelType(FuelType.ESSENCE)).thenReturn(List.of(garageDTO));

        mockMvc.perform(get("/api/garages/search/by-fuel")
                        .param("fuelType", "ESSENCE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Garage Fuel"));

        verify(garageService, times(1)).searchByFuelType(FuelType.ESSENCE);
    }

    @Test
    void shouldSearchByAccessory() throws Exception {
        GarageResponseDTO garageDTO = new GarageResponseDTO();
        garageDTO.setId(UUID.randomUUID());
        garageDTO.setName("Garage Accessory");

        when(garageService.searchByAccessory("GPS")).thenReturn(List.of(garageDTO));

        mockMvc.perform(get("/api/garages/search/by-accessory")
                        .param("accessoryName", "GPS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Garage Accessory"));

        verify(garageService, times(1)).searchByAccessory("GPS");
    }
}