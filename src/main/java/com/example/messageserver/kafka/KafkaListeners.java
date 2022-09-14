package com.example.messageserver.kafka;

import com.example.messageserver.model.BatteryStatisticDto;
import com.example.messageserver.service.PriceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Data
public class KafkaListeners {

    private static final String orderTopic = "price";

    private final ObjectMapper objectMapper;
    private final PriceService priceService;

    private CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = orderTopic, groupId = "name", errorHandler = "priceErrorHandler")
    public void consumeMessage(BatteryStatisticDto batteryDto) throws JsonProcessingException {
        if(batteryDto.getTotalWattCapacity()>50.0){
           throw new IllegalArgumentException("Too much watt capacity " + batteryDto.getTotalWattCapacity());
        }
        priceService.calculatePrice(batteryDto);
    }
}
