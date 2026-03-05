package com.renault.garage.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.controller.GarageController;
import com.renault.garage.dto.garage.GarageCreateDTO;
import com.renault.garage.dto.garage.GarageResponseDTO;
import com.renault.garage.services.GarageService;
import com.renault.garage.exception.NotFoundException;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GarageController.class)
class GarageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    GarageService garageService;

    private GarageCreateDTO buildCreateDto() {
        GarageCreateDTO dto = new GarageCreateDTO();
        dto.setName("Garage WebMvc");
        dto.setAddress("Rue Test");
        dto.setTelephone("0700000000");
        dto.setEmail("test@garage.com");
        dto.setOpeningTimes(java.util.Collections.emptyMap()); // selon votre modèle
        return dto;
    }

    private GarageResponseDTO buildResponse(UUID id, String name) {
        GarageResponseDTO res = new GarageResponseDTO();
        res.setId(id);
        res.setName(name);
        res.setAddress("Rue Test");
        res.setTelephone("0700000000");
        res.setEmail("test@garage.com");
        res.setOpeningTimes(java.util.Collections.emptyMap());
        return res;
    }

    @Test
    void createGarage_201() throws Exception {
        UUID id = UUID.randomUUID();
        GarageCreateDTO dto = buildCreateDto();
        GarageResponseDTO response = buildResponse(id, dto.getName());

        given(garageService.createGarage(any())).willReturn(response);

        mockMvc.perform(post("/api/v1/garages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/garages/" + id)) // si votre contrôleur met bien Location
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Garage WebMvc"));
    }

    @Test
    void createGarage_400() throws Exception {
        GarageCreateDTO invalid = new GarageCreateDTO();
        // name manquant / invalid selon vos annotations (@NotBlank, etc.)
        invalid.setAddress("Rue Test");
        invalid.setTelephone("0700000000");
        invalid.setEmail("test@garage.com");
        invalid.setOpeningTimes(java.util.Collections.emptyMap());

        mockMvc.perform(post("/api/v1/garages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
        // .andDo(print()); // utile si vous voulez voir les messages de validation
    }

    @Test
    void getGarageById_200() throws Exception {
        UUID id = UUID.randomUUID();
        GarageResponseDTO response = buildResponse(id, "Garage G1");

        given(garageService.getGarageById(eq(id))).willReturn(response);

        mockMvc.perform(get("/api/v1/garages/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Garage G1"));
    }

    @Test
    void getGarageById_400() throws Exception {
        UUID id = UUID.randomUUID();
        given(garageService.getGarageById(eq(id))).willThrow(new NotFoundException("Garage not found"));

        mockMvc.perform(get("/api/v1/garages/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllGarages_200() throws Exception {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        GarageResponseDTO g1 = buildResponse(id1, "G1");
        GarageResponseDTO g2 = buildResponse(id2, "G2");

        Page<GarageResponseDTO> page = new PageImpl<>(List.of(g1, g2), PageRequest.of(0, 2), 2);

        given(garageService.getAllGarages(any())).willReturn(page);

        mockMvc.perform(get("/api/v1/garages")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                // votre contrôleur retourne un Page<GarageResponseDTO> dans le body
                // selon vos Jackson/JacksonPageable serializers, cela peut s’afficher avec "content", "totalElements", etc.
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(id1.toString()))
                .andExpect(jsonPath("$.content[1].id").value(id2.toString()))
                .andExpect(header().string("X-Total-Count", "2")); // car vous ajoutez ce header dans votre contrôleur
    }

    @Test
    void updateGarage_200() throws Exception {
        UUID id = UUID.randomUUID();
        GarageCreateDTO updateDto = buildCreateDto();
        updateDto.setName("Garage Updated");

        GarageResponseDTO updated = buildResponse(id, "Garage Updated");
        given(garageService.updateGarage(eq(id), any())).willReturn(updated);

        mockMvc.perform(put("/api/v1/garages/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Garage Updated"));
    }

    @Test
    void updateGarage_404() throws Exception {
        UUID id = UUID.randomUUID();
        GarageCreateDTO updateDto = buildCreateDto();
        updateDto.setName("Garage Updated");

        given(garageService.updateGarage(eq(id), any()))
                .willThrow(new NotFoundException("Garage not found"));

        mockMvc.perform(put("/api/v1/garages/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteGarage_204() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(garageService).deleteGarage(eq(id));

        mockMvc.perform(delete("/api/v1/garages/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteGarage_404() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.doThrow(new NotFoundException("Garage not found"))
                .when(garageService).deleteGarage(eq(id));

        mockMvc.perform(delete("/api/v1/garages/{id}", id))
                .andExpect(status().isNotFound());
    }
}

