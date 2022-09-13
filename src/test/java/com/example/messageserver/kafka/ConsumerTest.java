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
import org.testng.annotations.AfterMethod;

import java.io.IOException;
import static org.mockito.ArgumentMatchers.any;


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

    @AfterMethod
    public void cleanUp() {
        Mockito.clearInvocations(priceService);
    }

    @Test
    public void shouldConsumeMessage() throws IOException, InterruptedException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        BatteryStatisticDto batteryStatisticDto = new BatteryStatisticDto();
        String message = ow.writeValueAsString(batteryStatisticDto);

        Mockito.when(objectMapper.readValue(message ,BatteryStatisticDto.class)).thenReturn(batteryStatisticDto);
        kafkaListeners.consumeMessage(message);
        Mockito.verify(priceService, Mockito.times(1))
                .calculatePrice(any(BatteryStatisticDto.class));
    }
    @Test
    public void shouldThrowIllegalArgumentException() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        BatteryStatisticDto batteryStatisticDto = new BatteryStatisticDto();
        String message = ow.writeValueAsString(batteryStatisticDto);
        batteryStatisticDto.setTotalWattCapacity(5000.0);
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Mockito.when(objectMapper.readValue(message ,BatteryStatisticDto.class)).thenReturn(batteryStatisticDto);
            kafkaListeners.consumeMessage(message);
        });

        Assertions.assertEquals("Too much watt capacity 5000.0", thrown.getMessage());

    }
}
