package com.renault.garage.service.unit;

import com.renault.garage.dto.accessory.AccessoryCreateDTO;
import com.renault.garage.dto.accessory.AccessoryResponseDTO;
import com.renault.garage.enums.AccessoryType;
import com.renault.garage.service.impl.AccessoryServiceImpl;
import com.renault.garage.entity.Accessory;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.mapper.AccessoryMapper;
import com.renault.garage.repository.AccessoryRepository;
import com.renault.garage.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class AccessoryServiceImplTest {

    @InjectMocks
    private AccessoryServiceImpl accessoryService;

    @Mock
    private AccessoryRepository accessoryRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private AccessoryMapper accessoryMapper;

    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vehicle = new Vehicle();
        vehicle.setId(UUID.randomUUID());
        vehicle.setAccessories(List.of());
    }

    @Test
    void testCreateAccessory_Success() {

        UUID vehicleId = UUID.randomUUID();

        AccessoryCreateDTO dto = new AccessoryCreateDTO();
        dto.setVehicleId(vehicleId);
        dto.setName("GPS");
        dto.setDescription("Navigation GPS");
        dto.setPrice(BigDecimal.valueOf(150.0));
        dto.setType(AccessoryType.SECURITY);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        Accessory mappedEntity = new Accessory();
        mappedEntity.setName("GPS");
        mappedEntity.setPrice(BigDecimal.valueOf(150.0));
        mappedEntity.setType(AccessoryType.SECURITY);

        Accessory saved = new Accessory();
        saved.setId(UUID.randomUUID());
        saved.setName("GPS");
        saved.setPrice(BigDecimal.valueOf(150.0));
        saved.setType(AccessoryType.SECURITY);
        saved.setVehicle(vehicle);

        AccessoryResponseDTO responseDTO = new AccessoryResponseDTO();
        responseDTO.setId(saved.getId());
        responseDTO.setName("GPS");
        responseDTO.setPrice(saved.getPrice());
        responseDTO.setType(saved.getType());
        responseDTO.setVehicleId(vehicle.getId());

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.of(vehicle));

        when(accessoryMapper.toEntity(dto))
                .thenReturn(mappedEntity);

        when(accessoryRepository.save(mappedEntity))
                .thenReturn(saved);

        when(accessoryMapper.toResponseDTO(saved))
                .thenReturn(responseDTO);

        AccessoryResponseDTO result = accessoryService.createAccessory(dto);

        assertNotNull(result);
        assertEquals("GPS", result.getName());
        assertEquals(saved.getId(), result.getId());
        assertEquals(vehicleId, result.getVehicleId());

        verify(accessoryMapper).toEntity(dto);
        verify(accessoryRepository).save(mappedEntity);
        verify(accessoryMapper).toResponseDTO(saved);
    }

    @Test
    void testUpdateAccessory_Success() {

        UUID accessoryId = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();

        AccessoryCreateDTO dto = new AccessoryCreateDTO();
        dto.setVehicleId(vehicleId);
        dto.setName("Updated GPS");
        dto.setDescription("Updated desc");
        dto.setPrice(BigDecimal.valueOf(200.0));
        dto.setType(AccessoryType.SECURITY);

        Accessory existing = new Accessory();
        existing.setId(accessoryId);
        existing.setName("Old GPS");

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        Accessory saved = new Accessory();
        saved.setId(accessoryId);
        saved.setName("Updated GPS");
        saved.setPrice(dto.getPrice());
        saved.setType(dto.getType());
        saved.setVehicle(vehicle);

        AccessoryResponseDTO responseDTO = new AccessoryResponseDTO();
        responseDTO.setId(accessoryId);
        responseDTO.setName("Updated GPS");
        responseDTO.setPrice(dto.getPrice());
        responseDTO.setVehicleId(vehicleId);

        when(accessoryRepository.findById(accessoryId))
                .thenReturn(Optional.of(existing));

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.of(vehicle));

        doNothing().when(accessoryMapper).updateEntityFromDTO(dto, existing);

        when(accessoryRepository.save(existing))
                .thenReturn(saved);

        when(accessoryMapper.toResponseDTO(saved))
                .thenReturn(responseDTO);

        AccessoryResponseDTO result = accessoryService.updateAccessory(accessoryId, dto);

        assertNotNull(result);
        assertEquals("Updated GPS", result.getName());
        assertEquals(accessoryId, result.getId());

        verify(accessoryRepository).findById(accessoryId);
        verify(vehicleRepository).findById(vehicleId);
        verify(accessoryMapper).updateEntityFromDTO(dto, existing);
        verify(accessoryRepository).save(existing);
        verify(accessoryMapper).toResponseDTO(saved);
    }

    @Test
    void testDeleteAccessory() {

        UUID accessoryId = UUID.randomUUID();

        when(accessoryRepository.existsById(accessoryId)).thenReturn(true);

        accessoryService.deleteAccessory(accessoryId);

        verify(accessoryRepository).existsById(accessoryId);
        verify(accessoryRepository).deleteById(accessoryId);
    }

    @Test
    void testGetAccessoriesByVehicle() {
        Accessory accessory = new Accessory();
        accessory.setId(UUID.randomUUID());
        accessory.setVehicle(vehicle);

        when(accessoryRepository.findByVehicleId(vehicle.getId())).thenReturn(List.of(accessory));
        when(accessoryMapper.toResponseDTO(accessory)).thenReturn(new AccessoryResponseDTO());

        List<AccessoryResponseDTO> result = accessoryService.getAccessoriesByVehicle(vehicle.getId());

        assertEquals(1, result.size());
    }

    @Test
    void testUpdateAccessory_NotFound() {
        UUID accessoryId = UUID.randomUUID();
        AccessoryCreateDTO dto = new AccessoryCreateDTO();
        when(accessoryRepository.findById(accessoryId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accessoryService.updateAccessory(accessoryId, dto));
    }
}