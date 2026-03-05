package com.renault.garage.dto.garage;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Data
@Getter @Setter
public class OpeningTimeResponseDTO {

    private UUID id;
    private LocalTime startTime;
    private LocalTime endTime;

}
