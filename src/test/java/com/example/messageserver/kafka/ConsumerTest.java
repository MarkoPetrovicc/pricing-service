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
        List<String> names = Arrays.asList("test1", "test2", "test3");
        BatteryStatisticDto batteryStatistic = BatteryStatisticDto.builder()
                .totalWattCapacity(200)
                .averageWattCapacity(100)
                .name(names).build();

        //ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        BatteryStatisticsDtoEvent batteryStatisticDto = BatteryStatisticsDtoEvent.builder()
                .id("id1")
                .batteryStatisticDto(batteryStatistic)
                .batteryStatisticsDtoOperation(BatteryStatisticsDtoOperation.LOW).build();
        //String message = ow.writeValueAsString(batteryStatisticDto);

       // Mockito.when(objectMapper.readValue(message ,BatteryStatisticsDtoEvent.class)).thenReturn(batteryStatisticDto);
        kafkaListeners.consumeMessage(batteryStatisticDto);
        Mockito.verify(priceService, Mockito.times(1))
                .calculatePrice(any(BatteryStatisticDto.class),any(String.class));
    }
    @Test
    public void shouldThrowIllegalArgumentException() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        BatteryStatisticDto batteryStatisticDto = new BatteryStatisticDto();
        BatteryStatisticsDtoEvent batteryStatisticsDtoEvent = new BatteryStatisticsDtoEvent();
        String message = ow.writeValueAsString(batteryStatisticDto);
        batteryStatisticDto.setTotalWattCapacity(5000.0);
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Mockito.when(objectMapper.readValue(message ,BatteryStatisticDto.class)).thenReturn(batteryStatisticDto);
            kafkaListeners.consumeMessage(batteryStatisticsDtoEvent);
        });

        Assertions.assertEquals("Too much watt capacity 5000.0", thrown.getMessage());

    }
}
