package com.example.messageserver.controller;

import com.example.messageserver.kafka.KafkaListeners;
import com.example.messageserver.model.BatteryPrice;
import com.example.messageserver.model.BatteryStatisticDto;
import com.example.messageserver.repository.PriceRepository;
import com.example.messageserver.service.PriceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;


import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PriceController.class)
@AutoConfigureMockMvc
class PriceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PriceService batteryService;

    @MockBean
    KafkaListeners kafkaProducer;

    @MockBean
    PriceRepository priceRepository;

    @MockBean
    KeycloakConfigResolver keycloakConfigResolver;

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
    void getPrices_success() throws Exception {
        List<BatteryPrice> batteries = Arrays.asList(batteryPrice1, batteryPrice2);

        Mockito.when(batteryService.getAll()).thenReturn(batteries);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/price")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].batteryName[0]").exists())
                .andExpect(jsonPath("$[0].batteryName[1]").exists())
                .andExpect(jsonPath("$[1].batteryName[0]").exists())
                .andExpect(jsonPath("$[1].batteryName[1]").exists())
                .andExpect(jsonPath("$[0].price").exists())
                .andExpect(jsonPath("$[1].price").exists())
                .andReturn();
    }

    @Test
    void getOnePrice_success() throws Exception {
       List<BatteryPrice> batteries = Arrays.asList(batteryPrice1, batteryPrice2);

        Mockito.when(batteryService.findOneByPrice(20.0)).thenReturn(batteries.get(0));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/price/findOne")
                .param("price", Double.toString(20.0))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.batteryName[0]").exists())
                .andExpect(jsonPath("$.batteryName[1]").exists())
                .andExpect(jsonPath("$.batteryName[2]").exists())
                .andExpect(jsonPath("$.price").exists())
                .andReturn();
    }

    }

