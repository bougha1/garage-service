package com.renault.garage.mapper;

import com.renault.garage.dto.garage.GarageCreateDTO;
import com.renault.garage.dto.garage.GarageResponseDTO;
import com.renault.garage.dto.garage.OpeningTimeDTO;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.OpeningTime;
import org.mapstruct.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {OpeningTimeMapper.class})
public interface GarageMapper {

    // CREATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "openingTimes", expression = "java(flattenOpeningMap(dto.getOpeningTimes(), openingTimeMapper))")
    Garage toEntity(GarageCreateDTO dto, @Context OpeningTimeMapper openingTimeMapper);


    default List<OpeningTime> flattenOpeningMap(Map<DayOfWeek, List<OpeningTimeDTO>> map,
                                                OpeningTimeMapper openingTimeMapper) {
        if (map == null || map.isEmpty()) return new ArrayList<>();
        List<OpeningTime> list = new ArrayList<>();
        map.forEach((day, slots) -> {
            if (slots != null) {
                for (OpeningTimeDTO slot : slots) {
                    OpeningTime ot = openingTimeMapper.toEntityWithDay(slot, day);
                    if (ot != null) list.add(ot);
                }
            }
        });
        return list;
    }

    @AfterMapping
    default void linkChildren(@MappingTarget Garage garage) {
        if (garage.getOpeningTimes() != null) {
            garage.getOpeningTimes().forEach(ot -> ot.setGarage(garage));
        }
    }



    // READ
    @Mapping(target = "openingTimes", expression = "java(groupOpeningMap(garage.getOpeningTimes(), openingTimeMapper))")
    GarageResponseDTO toResponseDTO(Garage garage, @Context OpeningTimeMapper openingTimeMapper);


    default Map<DayOfWeek, List<OpeningTimeDTO>> groupOpeningMap(List<OpeningTime> list,
                                                                 OpeningTimeMapper openingTimeMapper) {
        if (list == null || list.isEmpty()) return Collections.emptyMap();
        return list.stream()
                .collect(Collectors.groupingBy(
                        OpeningTime::getDayOfWeek,
                        Collectors.mapping(openingTimeMapper::toDTO, Collectors.toList())
                ));
    }


    // UPDATE

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "openingTimes", ignore = true) // on gère nous-mêmes
    void updateEntityFromDTO(GarageCreateDTO dto, @MappingTarget Garage entity);

    // Remplacement complet de la liste à partir de la map (PUT)
    default void replaceOpeningTimes(Garage entity,
                                     Map<DayOfWeek, List<OpeningTimeDTO>> map,
                                     OpeningTimeMapper openingTimeMapper) {
        entity.getOpeningTimes().clear(); // orphanRemoval = true => suppression propre
        if (map != null) {
            List<OpeningTime> newOnes = flattenOpeningMap(map, openingTimeMapper);
            for (OpeningTime ot : newOnes) {
                ot.setGarage(entity); // back-reference
            }
            entity.getOpeningTimes().addAll(newOnes);
        }
    }

}