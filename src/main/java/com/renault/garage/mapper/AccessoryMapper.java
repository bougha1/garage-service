package com.renault.garage.mapper;

import com.renault.garage.dto.accessory.AccessoryCreateDTO;
import com.renault.garage.dto.accessory.AccessoryResponseDTO;
import com.renault.garage.entity.Accessory;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AccessoryMapper {

    Accessory toEntity(AccessoryCreateDTO dto);

    AccessoryResponseDTO toResponseDTO(Accessory entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(AccessoryCreateDTO dto, @MappingTarget Accessory entity);

}