package com.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CarDto {
    private String registration;
    private String colour;
    private String model;
    private String fuelType;

    public CarDto() {
    }
}
