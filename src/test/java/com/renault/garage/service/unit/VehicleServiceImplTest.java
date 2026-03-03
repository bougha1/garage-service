package com.renault.garage.service.unit;

import com.renault.garage.dto.vehicle.VehicleCreateDTO;
import com.renault.garage.dto.vehicle.VehicleResponseDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.enums.FuelType;
import com.renault.garage.exception.NotFoundException;
import com.renault.garage.exception.QuotaExceededException;
import com.renault.garage.kafka.producer.VehicleProducer;
import com.renault.garage.mapper.VehicleMapper;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import com.renault.garage.service.impl.VehicleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehicleServiceImplTest {

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private VehicleProducer vehicleProducer;

    private Garage garage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        garage = new Garage();
        garage.setId(UUID.randomUUID());
        garage.setName("Garage Test");
        garage.setVehicles(new ArrayList<>());
    }

    @Test
    void testCreateVehicle_Success() {
        UUID garageId = UUID.randomUUID();
        UUID vehicleId = UUID.randomUUID();
        VehicleCreateDTO dto = new VehicleCreateDTO();
        dto.setGarageId(garageId);
        dto.setBrand("Renault");
        dto.setModel("Clio");

        Garage garage = new Garage();
        garage.setId(garageId);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        when(garageRepository.findById(garageId)).thenReturn(Optional.of(garage));
        when(vehicleMapper.toEntity(dto)).thenReturn(vehicle);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        VehicleResponseDTO response = vehicleService.createVehicle(dto);

        verify(vehicleProducer).sendVehicleCreatedEvent(any());
    }

    @Test
    void testCreateVehicle_QuotaExceeded() {
        // Fill garage with 50 vehicles to hit quota
        for (int i = 0; i < 50; i++) {
            garage.getVehicles().add(new Vehicle());
        }

        VehicleCreateDTO dto = new VehicleCreateDTO();
        dto.setGarageId(garage.getId());
        dto.setBrand("Renault");
        dto.setModel("Clio");
        dto.setFuelType(FuelType.DIESEL);

        when(garageRepository.findById(garage.getId())).thenReturn(Optional.of(garage));

        assertThrows(QuotaExceededException.class, () -> vehicleService.createVehicle(dto));
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    void testUpdateVehicle_Success() {
        UUID vehicleId = UUID.randomUUID();
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        vehicle.setGarage(garage);

        VehicleCreateDTO dto = new VehicleCreateDTO();
        dto.setGarageId(garage.getId());
        dto.setBrand("Renault Updated");
        dto.setModel("Clio");
        dto.setFuelType(FuelType.ESSENCE);

        VehicleResponseDTO responseDTO = new VehicleResponseDTO();
        responseDTO.setId(vehicleId);
        responseDTO.setBrand("Renault Updated");

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        doNothing().when(vehicleMapper).updateEntityFromDTO(dto, vehicle);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.toResponseDTO(vehicle)).thenReturn(responseDTO);

        VehicleResponseDTO result = vehicleService.updateVehicle(vehicleId, dto);

        assertEquals("Renault Updated", result.getBrand());
        verify(vehicleRepository, times(1)).save(vehicle);
    }

    @Test
    void testDeleteVehicle() {
        UUID vehicleId = UUID.randomUUID();
        Vehicle vehicle = new Vehicle();
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        vehicleService.deleteVehicle(vehicleId);

        verify(vehicleRepository).delete(vehicle);
    }

    @Test
    void testGetVehiclesByGarage() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(UUID.randomUUID());
        vehicle.setBrand("Renault");
        vehicle.setGarage(garage);

        when(vehicleRepository.findByGarageId(garage.getId())).thenReturn(List.of(vehicle));
        when(vehicleMapper.toResponseDTO(vehicle)).thenReturn(new VehicleResponseDTO());

        List<VehicleResponseDTO> result = vehicleService.getVehiclesByGarage(garage.getId());

        assertEquals(1, result.size());
    }

    @Test
    void testGetVehiclesByModel() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(UUID.randomUUID());
        vehicle.setModel("Clio");

        when(vehicleRepository.findByModel("Clio")).thenReturn(List.of(vehicle));
        when(vehicleMapper.toResponseDTO(vehicle)).thenReturn(new VehicleResponseDTO());

        List<VehicleResponseDTO> result = vehicleService.getVehiclesByModel("Clio");

        assertEquals(1, result.size());
    }

    @Test
    void testUpdateVehicle_NotFound() {
        UUID vehicleId = UUID.randomUUID();
        VehicleCreateDTO dto = new VehicleCreateDTO();
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> vehicleService.updateVehicle(vehicleId, dto));
    }
}
