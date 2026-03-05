package com.renault.garage.service.integration;

import com.renault.garage.dto.accessory.AccessoryCreateDTO;
import com.renault.garage.dto.accessory.AccessoryResponseDTO;
import com.renault.garage.entity.Accessory;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.enums.AccessoryType;
import com.renault.garage.enums.FuelType;
import com.renault.garage.repository.AccessoryRepository;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import com.renault.garage.services.AccessoryService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AccessoryServiceIntegration {


    @Autowired
    private AccessoryService accessoryService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private AccessoryRepository accessoryRepository;

    private Vehicle createVehicle() {
        Garage garage = new Garage();
        garage.setName("Orig Garage");
        garage.setAddress("123 rue");
        garage.setTelephone("0678954326");
        garage.setEmail("orig@test.com");
        garageRepository.save(garage);

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("Renault");
        vehicle.setModel("Clio");
        vehicle.setFuelType(FuelType.DIESEL);
        vehicle.setGarage(garage);
        return vehicleRepository.save(vehicle);
    }

    @Test
    void shouldCreateAccessory() {
        Vehicle vehicle = createVehicle();

        AccessoryCreateDTO dto = new AccessoryCreateDTO();
        dto.setName("Roof Rack");
        dto.setVehicleId(vehicle.getId());

        AccessoryResponseDTO response = accessoryService.createAccessory(dto);

        assertNotNull(response.getId());
        assertEquals("Roof Rack", response.getName());
        assertEquals(vehicle.getId(), response.getVehicleId());
    }

    @Test
    void shouldGetAccessoryById() {
        Vehicle vehicle = createVehicle();

        Accessory accessory = new Accessory();
        accessory.setName("GPS");
        accessory.setVehicle(vehicle);
        accessory = accessoryRepository.save(accessory);

        AccessoryResponseDTO found =
                accessoryService.getAccessoryById(accessory.getId());

        assertEquals(accessory.getId(), found.getId());
        assertEquals("GPS", found.getName());
    }

    @Test
    void shouldUpdateAccessory() {
        Vehicle vehicle = createVehicle();

        Accessory accessory = new Accessory();
        accessory.setName("Old Name");
        accessory.setVehicle(vehicle);
        accessory = accessoryRepository.save(accessory);

        AccessoryCreateDTO updateDto = new AccessoryCreateDTO();
        updateDto.setName("Updated Name");
        updateDto.setVehicleId(vehicle.getId());

        AccessoryResponseDTO updated =
                accessoryService.updateAccessory(accessory.getId(), updateDto);

        assertEquals("Updated Name", updated.getName());

        Accessory updatedEntity =
                accessoryRepository.findById(accessory.getId()).orElseThrow();

        assertEquals("Updated Name", updatedEntity.getName());
    }

    @Test
    void shouldDeleteAccessory() {
        Vehicle vehicle = createVehicle();

        Accessory accessory = new Accessory();
        accessory.setName("To Delete");
        accessory.setPrice(BigDecimal.valueOf(100));
        accessory.setType(AccessoryType.SECURITY);
        accessory.setVehicle(vehicle);
        accessory = accessoryRepository.save(accessory);

        UUID id = accessory.getId();

        accessoryService.deleteAccessory(id);

        assertFalse(accessoryRepository.findById(id).isPresent());
    }

    @Test
    void shouldGetAccessoriesByVehicle() {
        Vehicle vehicle = createVehicle();
        Vehicle vehicle2 = createVehicle(); // second vehicle

        Accessory a1 = new Accessory();
        a1.setName("GPS");
        a1.setPrice(BigDecimal.valueOf(900));
        a1.setType(AccessoryType.MULTIMEDIA);
        a1.setVehicle(vehicle);
        accessoryRepository.save(a1);

        Accessory a2 = new Accessory();
        a2.setName("Camera");
        a2.setPrice(BigDecimal.valueOf(400));
        a2.setType(AccessoryType.SECURITY);
        a2.setVehicle(vehicle);
        accessoryRepository.save(a2);

        Accessory a3 = new Accessory();
        a3.setName("Seat Cover");
        a3.setPrice(BigDecimal.valueOf(1200));
        a3.setType(AccessoryType.COMFORT);
        a3.setVehicle(vehicle2);
        accessoryRepository.save(a3);

        List<AccessoryResponseDTO> list =
                accessoryService.getAccessoriesByVehicle(vehicle.getId());

        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(x -> x.getName().equals("GPS")));
        assertTrue(list.stream().anyMatch(x -> x.getName().equals("Camera")));
    }

}
