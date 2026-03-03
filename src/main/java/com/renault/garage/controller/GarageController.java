package com.renault.garage.controller;

import com.renault.garage.dto.garage.*;
import com.renault.garage.enums.FuelType;
import com.renault.garage.services.GarageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/garages")
public class GarageController {

    private final GarageService garageService;

    public GarageController(GarageService garageService) {
        this.garageService = garageService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GarageResponseDTO createGarage(@Valid @RequestBody GarageCreateDTO dto) {
        return garageService.createGarage(dto);
    }

    @PutMapping("/{id}")
    public GarageResponseDTO updateGarage(@PathVariable UUID id,
                                          @Valid @RequestBody GarageCreateDTO dto) {
        return garageService.updateGarage(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGarage(@PathVariable UUID id) {
        garageService.deleteGarage(id);
    }

    @GetMapping("/{id}")
    public GarageResponseDTO getGarage(@PathVariable UUID id) {
        return garageService.getGarageById(id);
    }

    @GetMapping
    public Page<GarageResponseDTO> getAllGarages(Pageable pageable) {
        return garageService.getAllGarages(pageable);
    }

    @GetMapping("/search/by-fuel")
    public List<GarageResponseDTO> searchByFuelType(
            @RequestParam FuelType fuelType) {
        return garageService.searchByFuelType(fuelType);
    }

    @GetMapping("/search/by-accessory")
    public List<GarageResponseDTO> searchByAccessory(
            @RequestParam String accessoryName) {
        return garageService.searchByAccessory(accessoryName);
    }
}