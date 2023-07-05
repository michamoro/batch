package com.batch.processor;

import com.batch.dto.CarDto;
import com.batch.model.CarEntity;
import org.springframework.batch.item.ItemProcessor;

import java.util.UUID;

public class CarItemProcessor implements ItemProcessor<CarDto, CarEntity> {
    @Override
    public CarEntity process(CarDto item) throws Exception {
        return CarEntity.builder()
                .id(UUID.randomUUID().toString())
                .colour(item.getColour())
                .model(item.getModel())
                .fuelType(item.getFuelType())
                .registration(item.getRegistration())
                .build();
    }
}
