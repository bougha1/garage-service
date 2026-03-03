package com.renault.garage.service.unit;

import com.renault.garage.dto.accessory.AccessoryCreateDTO;
import com.renault.garage.dto.accessory.AccessoryResponseDTO;
import com.renault.garage.enums.AccessoryType;
import com.renault.garage.services.impl.AccessoryServiceImpl;
import com.renault.garage.entity.Accessory;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.mapper.AccessoryMapper;
import com.renault.garage.repository.AccessoryRepository;
import com.renault.garage.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

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
        AccessoryCreateDTO dto = new AccessoryCreateDTO();
        dto.setVehicleId(vehicle.getId());
        dto.setName("GPS");
        dto.setDescription("Navigation GPS");
        dto.setPrice(150.0);
        dto.setType(AccessoryType.SECURITY);

        Accessory accessory = new Accessory();
        accessory.setId(UUID.randomUUID());

        AccessoryResponseDTO responseDTO = new AccessoryResponseDTO();
        responseDTO.setId(accessory.getId());
        responseDTO.setName("GPS");

        when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));
        when(accessoryMapper.toEntity(dto)).thenReturn(accessory);
        when(accessoryRepository.save(accessory)).thenReturn(accessory);
        when(accessoryMapper.toResponseDTO(accessory)).thenReturn(responseDTO);

        AccessoryResponseDTO result = accessoryService.createAccessory(dto);

        assertNotNull(result);
        assertEquals("GPS", result.getName());
        verify(accessoryRepository, times(1)).save(accessory);
    }

    @Test
    void testUpdateAccessory_Success() {
        UUID accessoryId = UUID.randomUUID();
        Accessory accessory = new Accessory();
        accessory.setId(accessoryId);
        accessory.setVehicle(vehicle);

        AccessoryCreateDTO dto = new AccessoryCreateDTO();
        dto.setVehicleId(vehicle.getId());
        dto.setName("GPS Updated");
        dto.setDescription("Updated Navigation GPS");
        dto.setPrice(160.0);
        dto.setType(AccessoryType.SECURITY);

        AccessoryResponseDTO responseDTO = new AccessoryResponseDTO();
        responseDTO.setId(accessoryId);
        responseDTO.setName("GPS Updated");

        when(accessoryRepository.findById(accessoryId)).thenReturn(Optional.of(accessory));
        doNothing().when(accessoryMapper).updateEntityFromDTO(dto, accessory);
        when(accessoryRepository.save(accessory)).thenReturn(accessory);
        when(accessoryMapper.toResponseDTO(accessory)).thenReturn(responseDTO);

        AccessoryResponseDTO result = accessoryService.updateAccessory(accessoryId, dto);

        assertEquals("GPS Updated", result.getName());
        verify(accessoryRepository, times(1)).save(accessory);
    }

    @Test
    void testDeleteAccessory() {
        UUID accessoryId = UUID.randomUUID();
        Accessory accessory = new Accessory();
        accessory.setId(accessoryId);

        when(accessoryRepository.findById(accessoryId)).thenReturn(Optional.of(accessory));

        accessoryService.deleteAccessory(accessoryId);

        verify(accessoryRepository).delete(accessory);
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