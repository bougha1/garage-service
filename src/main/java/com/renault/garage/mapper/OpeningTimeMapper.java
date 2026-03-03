package com.renault.garage.mapper;

import com.renault.garage.dto.garage.OpeningTimeDTO;
import com.renault.garage.entity.OpeningTime;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OpeningTimeMapper {

    OpeningTime toEntity(OpeningTimeDTO dto);

    OpeningTimeDTO toDTO(OpeningTime entity);
}