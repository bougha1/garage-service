package com.renault.garage.mapper;

import com.renault.garage.dto.accessory.AccessoryCreateDTO;
import com.renault.garage.dto.accessory.AccessoryResponseDTO;
import com.renault.garage.entity.Accessory;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AccessoryMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    Accessory toEntity(AccessoryCreateDTO dto);

    @Mapping(target = "vehicleId", source = "vehicle.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AccessoryResponseDTO toResponseDTO(Accessory entity);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    void updateEntityFromDTO(AccessoryCreateDTO dto, @MappingTarget Accessory entity);

}