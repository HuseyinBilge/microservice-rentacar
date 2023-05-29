package com.kodlamaio.inventoryservice.business.dto.responses.update;

import com.kodlamaio.inventoryservice.entities.enums.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateCarResponse {
    private UUID id;
    private UUID modelId;


    private int modelYear;


    private String plate;


    private double dailyPrice;
    private State state;

}
