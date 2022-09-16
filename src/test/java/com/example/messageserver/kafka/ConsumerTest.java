package com.example.messageserver.kafka;

import com.example.messageserver.model.BatteryStatisticDto;
import com.example.messageserver.service.PriceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Ignore;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@Ignore
public class ConsumerTest {

    MockConsumer<String, String> consumer;

    @Mock
    private PriceService priceService;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    @Autowired
    private KafkaListeners kafkaListeners;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void shouldConsumeMessage() throws IOException, InterruptedException {
        List<String> names = List.of("test1", "test2", "test3");
        BatteryStatisticDto batteryStatistic = BatteryStatisticDto.builder()
                .totalWattCapacity(200)
                .averageWattCapacity(100)
                .name(names).build();
        BatteryStatisticsDtoEvent batteryStatisticDto = BatteryStatisticsDtoEvent.builder()
                .eventUid(UUID.randomUUID())
                .batteryStatisticDto(batteryStatistic)
                .batteryStatisticsDtoOperation(BatteryStatisticsDtoOperation.LOW).build();
        kafkaListeners.consumeMessage(batteryStatisticDto);
        Mockito.verify(priceService, Mockito.times(1))
                .calculatePrice(any(BatteryStatisticDto.class),any(String.class));
    }
    @Test
    public void shouldThrowIllegalArgumentException()  {
        List<String> names = List.of("test1", "test2", "test3");
        BatteryStatisticDto batteryStatisticDto = new BatteryStatisticDto();
        BatteryStatisticDto batteryStatistic = BatteryStatisticDto.builder()
                .totalWattCapacity(10000.0)
                .averageWattCapacity(222)
                .name(names).build();
        BatteryStatisticsDtoEvent batteryStatisticsDtoEvent = BatteryStatisticsDtoEvent.builder()
                .eventUid(UUID.randomUUID())
                .batteryStatisticDto(batteryStatistic)
                .batteryStatisticsDtoOperation(BatteryStatisticsDtoOperation.MEDIUM).build();
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            kafkaListeners.consumeMessage(batteryStatisticsDtoEvent);
        });

        Assertions.assertEquals("Too much watt capacity 10000.0", thrown.getMessage());

    }
}
