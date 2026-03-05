package com.renault.garage.mapper;

import com.renault.garage.dto.garage.OpeningTimeDTO;
import com.renault.garage.entity.OpeningTime;
import org.mapstruct.*;

import java.time.DayOfWeek;

@Mapper(componentModel = "spring")
public interface OpeningTimeMapper {

    OpeningTime toEntity(OpeningTimeDTO dto);

    OpeningTimeDTO toDTO(OpeningTime entity);


    default OpeningTime toEntityWithDay(OpeningTimeDTO dto, DayOfWeek day) {
        if (dto == null || day == null) return null;
        OpeningTime ot = new OpeningTime();
        ot.setDayOfWeek(day);
        ot.setStartTime(dto.getStartTime());
        ot.setEndTime(dto.getEndTime());
        return ot;
    }

}