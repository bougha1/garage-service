package com.renault.garage.service.integration;

import com.renault.garage.dto.garage.GarageCreateDTO;
import com.renault.garage.dto.garage.GarageResponseDTO;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.services.GarageService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GarageServiceIntegration {

    @Autowired
    private GarageService garageService;

    @Test
    void shouldCreateGarage() {
        GarageCreateDTO dto = new GarageCreateDTO();
        dto.setName("Garage");
        dto.setAddress("123 rue");
        dto.setTelephone("0678954326");
        dto.setEmail("test@test.com");
        dto.setOpeningTimes(Collections.emptyMap());

        GarageResponseDTO responseDTO = garageService.createGarage(dto);

        assertNotNull(responseDTO.getId());
        assertEquals("Garage", responseDTO.getName());
    }

    @Test
    void shouldUpdateGarage() {
        GarageCreateDTO dto = new GarageCreateDTO();
        dto.setName("Orig Garage");
        dto.setAddress("123 rue");
        dto.setTelephone("0678954326");
        dto.setEmail("orig@test.com");
        dto.setOpeningTimes(Collections.emptyMap());

        GarageResponseDTO responseDTO = garageService.createGarage(dto);

        dto.setName("Updated Garage");
        GarageResponseDTO updated = garageService.updateGarage(responseDTO.getId(), dto);

        assertEquals("Updated Garage", updated.getName());
    }

    @Test
    void shouldDeleteGarage() {
        GarageCreateDTO dto = new GarageCreateDTO();
        dto.setName("Garage");
        dto.setAddress("123 rue");
        dto.setTelephone("0678954326");
        dto.setEmail("delete@test.com");
        dto.setOpeningTimes(Collections.emptyMap());

        GarageResponseDTO responseDTO = garageService.createGarage(dto);
        garageService.deleteGarage(responseDTO.getId());

        assertThrows(RuntimeException.class, () -> garageService.getGarageById(responseDTO.getId()));
    }
}
