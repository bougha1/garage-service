package com.renault.garage.controller;

import com.renault.garage.dto.accessory.*;
import com.renault.garage.services.AccessoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accessories")
public class AccessoryController {

    private final AccessoryService accessoryService;

    public AccessoryController(AccessoryService accessoryService) {
        this.accessoryService = accessoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccessoryResponseDTO createAccessory(@Valid @RequestBody AccessoryCreateDTO dto) {
        return accessoryService.createAccessory(dto);
    }

    @PutMapping("/{id}")
    public AccessoryResponseDTO updateAccessory(@PathVariable UUID id,
                                                @Valid @RequestBody AccessoryCreateDTO dto) {
        return accessoryService.updateAccessory(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccessory(@PathVariable UUID id) {
        accessoryService.deleteAccessory(id);
    }

    @GetMapping("/{id}")
    public AccessoryResponseDTO getAccessory(@PathVariable UUID id) {
        return accessoryService.getAccessoryById(id);
    }

    @GetMapping("/by-vehicle/{vehicleId}")
    public List<AccessoryResponseDTO> getAccessoriesByVehicle(@PathVariable UUID vehicleId) {
        return accessoryService.getAccessoriesByVehicle(vehicleId);
    }
}