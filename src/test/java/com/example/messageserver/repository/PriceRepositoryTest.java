package com.example.messageserver.repository;

import com.example.messageserver.model.BatteryPrice;
import com.example.messageserver.model.BatteryStatisticDto;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
public class PriceRepositoryTest {

    @Autowired
    private PriceRepository priceRepository;
    List<String> names = Arrays.asList("test1", "test2", "test3");

    BatteryPrice batteryPrice1 = BatteryPrice.builder()
            .id(new ObjectId("6319b40c1af1edfd8a6a1fbb"))
            .batteryName(names)
            .price(12.0)
            .build();

    BatteryPrice batteryPrice2 = BatteryPrice.builder()
            .id(new ObjectId("6319b3fdcc03d4d86505b6e0"))
            .batteryName(names)
            .price(15.0)
            .build();

    BatteryStatisticDto batteryStatisticDto4 = BatteryStatisticDto.builder()
            .name(names)
            .totalWattCapacity(6.0)
            .averageWattCapacity(11.0)
            .build();

    BatteryStatisticDto batteryStatisticDto5 = BatteryStatisticDto.builder()
            .name(names)
            .totalWattCapacity(1.0)
            .averageWattCapacity(2.0)
            .build();

    @Test
    void shouldFindAll(){
        List<BatteryPrice> batteryPrices = priceRepository.findAll();

        assertEquals(31, batteryPrices.size());
    }

}
