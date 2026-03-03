package com.renault.garage.mapper;

import com.renault.garage.dto.garage.GarageCreateDTO;
import com.renault.garage.dto.garage.GarageResponseDTO;
import com.renault.garage.dto.garage.OpeningTimeDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.OpeningTime;
import org.mapstruct.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {OpeningTimeMapper.class})
public interface GarageMapper {

    // CREATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "openingTimes", ignore = true)
    Garage toEntity(GarageCreateDTO dto);

    // READ
    @Mapping(target = "openingTimes", ignore = true)
    GarageResponseDTO toResponseDTO(Garage garage);

    // UPDATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "openingTimes", ignore = true)
    void updateEntityFromDTO(GarageCreateDTO dto,
                             @MappingTarget Garage entity);

    default List<OpeningTime> mapOpeningTimes(
            Map<DayOfWeek, List<OpeningTimeDTO>> dtoMap,
            Garage garage
    ) {
        List<OpeningTime> result = new ArrayList<>();

        if (dtoMap == null) return result;

        dtoMap.forEach((day, times) -> {
            times.forEach(t -> {
                OpeningTime ot = new OpeningTime();
                ot.setDayOfWeek(day);
                ot.setStartTime(t.getStartTime());
                ot.setEndTime(t.getEndTime());
                ot.setGarage(garage);
                result.add(ot);
            });
        });
        return result;
    }

    default Map<DayOfWeek, List<OpeningTimeDTO>> mapOpeningTimesToDTO(
            List<OpeningTime> entities
    ) {
        return entities.stream()
                .collect(Collectors.groupingBy(
                        OpeningTime::getDayOfWeek,
                        Collectors.mapping(ot -> {
                            OpeningTimeDTO dto = new OpeningTimeDTO();
                            dto.setStartTime(ot.getStartTime());
                            dto.setEndTime(ot.getEndTime());
                            return dto;
                        }, Collectors.toList())
                ));
    }
}