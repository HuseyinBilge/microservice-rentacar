package com.kodlamaio.commonpackage.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CarInfoClientResponse {
    private String modelName;
    private String brandName;
    private String plate;
    private int modelYear;
}
