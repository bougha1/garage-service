package com.renault.garage.controller;

import com.renault.garage.dto.garage.*;
import com.renault.garage.enums.FuelType;
import com.renault.garage.services.GarageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/garages")
public class GarageController {

    private final GarageService garageService;

    public GarageController(GarageService garageService) {
        this.garageService = garageService;
    }

    @PostMapping
    public ResponseEntity<GarageResponseDTO> createGarage(@Valid @RequestBody GarageCreateDTO dto) {
        GarageResponseDTO created = garageService.createGarage(dto);
        URI location = URI.create(String.format("/api/v1/garages/%s", created.getId()));
        return ResponseEntity
                .created(location)
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GarageResponseDTO> getGarageById(@PathVariable UUID id) {
        GarageResponseDTO dto = garageService.getGarageById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<GarageResponseDTO>> getAllGarages(Pageable pageable) {
        Page<GarageResponseDTO> page = garageService.getAllGarages(pageable);
        // Optionally add pagination headers if you like (X-Total-Count, etc.)
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));
        return new ResponseEntity<>(page, headers, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GarageResponseDTO> updateGarage(
            @PathVariable UUID id,
            @Valid @RequestBody GarageCreateDTO dto
    ) {
        GarageResponseDTO updated = garageService.updateGarage(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGarage(@PathVariable UUID id) {
        garageService.deleteGarage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/by-fuel")
    public ResponseEntity<List<GarageResponseDTO>> searchByFuelType(@RequestParam FuelType fuelType) {
        List<GarageResponseDTO> list = garageService.searchByFuelType(fuelType);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/search/by-accessory")
    public ResponseEntity<List<GarageResponseDTO>> searchByAccessory(@RequestParam("name") String accessoryName) {
        if (!StringUtils.hasText(accessoryName)) {
            return ResponseEntity.badRequest().build();
        }
        List<GarageResponseDTO> list = garageService.searchByAccessory(accessoryName);
        return ResponseEntity.ok(list);
    }

}