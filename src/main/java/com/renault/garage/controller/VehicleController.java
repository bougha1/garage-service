package com.renault.garage.controller;

import com.renault.garage.dto.vehicle.*;
import com.renault.garage.services.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleResponseDTO createVehicle(@Valid @RequestBody VehicleCreateDTO dto) {
        return vehicleService.createVehicle(dto);
    }

    @PutMapping("/{id}")
    public VehicleResponseDTO updateVehicle(@PathVariable UUID id,
                                            @Valid @RequestBody VehicleCreateDTO dto) {
        return vehicleService.updateVehicle(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVehicle(@PathVariable UUID id) {
        vehicleService.deleteVehicle(id);
    }

    @GetMapping("/{id}")
    public VehicleResponseDTO getVehicle(@PathVariable UUID id) {
        return vehicleService.getVehicleById(id);
    }

    @GetMapping("/by-garage/{garageId}")
    public List<VehicleResponseDTO> getVehiclesByGarage(@PathVariable UUID garageId) {
        return vehicleService.getVehiclesByGarage(garageId);
    }

    @GetMapping("/by-model")
    public List<VehicleResponseDTO> getVehiclesByModel(@RequestParam String model) {
        return vehicleService.getVehiclesByModel(model);
    }
}