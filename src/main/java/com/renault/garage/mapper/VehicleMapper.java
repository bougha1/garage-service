package com.renault.garage.mapper;

import com.renault.garage.dto.vehicle.VehicleCreateDTO;
import com.renault.garage.dto.vehicle.VehicleResponseDTO;
import com.renault.garage.entity.Vehicle;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    // CREATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "garage", ignore = true)
    @Mapping(target = "accessories", ignore = true)
    @Mapping(source = "manufactureYear", target = "manufactureYear")
    Vehicle toEntity(VehicleCreateDTO dto);

    // READ
    VehicleResponseDTO toResponseDTO(Vehicle vehicle);

    // UPDATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "garage", ignore = true)
    @Mapping(target = "accessories", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(VehicleCreateDTO dto,
                             @MappingTarget Vehicle entity);
}