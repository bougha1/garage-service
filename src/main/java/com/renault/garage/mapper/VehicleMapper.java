package com.renault.garage.mapper;

import com.renault.garage.dto.vehicle.VehicleCreateDTO;
import com.renault.garage.dto.vehicle.VehicleResponseDTO;
import com.renault.garage.entity.Vehicle;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "garage", ignore = true)
    @Mapping(target = "accessories", ignore = true)
    @Mapping(source = "manufactureYear", target = "manufactureYear")
    Vehicle toEntity(VehicleCreateDTO dto);

    VehicleResponseDTO toResponseDTO(Vehicle vehicle);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "garage", ignore = true)
    @Mapping(target = "accessories", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(VehicleCreateDTO dto,
                             @MappingTarget Vehicle entity);
}