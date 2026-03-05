package com.renault.garage.service.unit;

import com.renault.garage.dto.garage.GarageCreateDTO;
import com.renault.garage.dto.garage.GarageResponseDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.mapper.GarageMapper;
import com.renault.garage.mapper.OpeningTimeMapper;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.service.impl.GarageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GarageServiceImplTest {

    @InjectMocks
    private GarageServiceImpl garageService;

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private GarageMapper garageMapper;

    @Mock
    private OpeningTimeMapper openingTimeMapper;

    private Garage garage;
    private GarageCreateDTO garageCreateDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        garageCreateDTO = new GarageCreateDTO();
        garageCreateDTO.setName("Garage A");
        garageCreateDTO.setAddress("123 Rue Exemple");
        garageCreateDTO.setTelephone("0102030405");
        garageCreateDTO.setEmail("garage@example.com");
        garageCreateDTO.setOpeningTimes(new HashMap<>());

        garage = new Garage();
        garage.setId(UUID.randomUUID());
        garage.setName(garageCreateDTO.getName());
        garage.setAddress(garageCreateDTO.getAddress());
        garage.setTelephone(garageCreateDTO.getTelephone());
        garage.setEmail(garageCreateDTO.getEmail());
        garage.setOpeningTimes(new ArrayList<>());
    }

    @Test
    void createGarage_ShouldReturnDTO() {

        when(garageMapper.toEntity(eq(garageCreateDTO), eq(openingTimeMapper)))
                .thenReturn(garage);

        when(garageRepository.save(garage)).thenReturn(garage);

        when(garageMapper.toResponseDTO(eq(garage), eq(openingTimeMapper)))
                .thenReturn(new GarageResponseDTO());

        GarageResponseDTO result = garageService.createGarage(garageCreateDTO);

        assertNotNull(result);

        verify(garageMapper, times(1)).toEntity(garageCreateDTO, openingTimeMapper);
        verify(garageRepository, times(1)).save(garage);
        verify(garageMapper, times(1)).toResponseDTO(garage, openingTimeMapper);
    }

    @Test
    void getGarageById_ShouldReturnGarage() {

        when(garageRepository.findById(garage.getId()))
                .thenReturn(Optional.of(garage));

        when(garageMapper.toResponseDTO(eq(garage), eq(openingTimeMapper)))
                .thenReturn(new GarageResponseDTO());

        GarageResponseDTO result = garageService.getGarageById(garage.getId());

        assertNotNull(result);

        verify(garageRepository, times(1)).findById(garage.getId());
        verify(garageMapper, times(1)).toResponseDTO(garage, openingTimeMapper);
    }

    @Test
    void getGarageById_ShouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();
        when(garageRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> garageService.getGarageById(id));
        verify(garageRepository, times(1)).findById(id);
    }

    @Test
    void deleteGarage_ShouldCallRepository() {
        UUID id = garage.getId();
        when(garageRepository.existsById(id)).thenReturn(true);

        garageService.deleteGarage(id);

        verify(garageRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteGarage_ShouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();
        when(garageRepository.existsById(id)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> garageService.deleteGarage(id));
        verify(garageRepository, never()).deleteById(id);
    }
}