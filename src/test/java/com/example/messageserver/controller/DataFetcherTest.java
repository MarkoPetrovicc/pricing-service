package com.example.messageserver.controller;

import com.example.messageserver.graphql.DataFetcher;
import com.example.messageserver.model.BatteryPrice;
import com.example.messageserver.model.graphql.client.PricesGraphQLQuery;
import com.example.messageserver.model.graphql.client.PricesProjectionRoot;
import com.example.messageserver.model.graphql.client.SavePriceGraphQLQuery;
import com.example.messageserver.model.graphql.types.BatteryPriceInput;
import com.example.messageserver.service.PriceService;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Import({DgsAutoConfiguration.class, DataFetcher.class, PriceService.class})
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DataFetcherTest {


    @MockBean
    PriceService priceService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;
    List<String> names = Arrays.asList("test1","test2","test3");
    BatteryPrice priceToCreate = BatteryPrice.builder()
            .id(new ObjectId("631efbf2c22f3763c63a6a84"))
            .price(300.0)
            .batteryName(names)
            .build();

    List<BatteryPrice> batteryPrices = List.of(priceToCreate, priceToCreate, priceToCreate);

    @BeforeEach
    public void before(){
        Mockito.when(priceService.getAll()).thenAnswer(invocation -> List.of(new BatteryPrice()));
    }

    @Test
    public void savePrice() {
        BatteryPrice priceToCreate = BatteryPrice.builder()
                .id(new ObjectId("631efbf2c22f3763c63a6a84"))
                .price(300.0)
                .batteryName(names)
                .build();

        BatteryPriceInput batteryPriceInput = new BatteryPriceInput();
        batteryPriceInput.setPrice(200);
        batteryPriceInput.setNames("marko");
        Mockito.when(priceService.savePrice(batteryPriceInput)).thenReturn(priceToCreate);


        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
               SavePriceGraphQLQuery.newRequest().batteryPriceInput(batteryPriceInput).build(),
                new PricesProjectionRoot().price());


        var user = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQLQueryRequest.serialize(),
                "data.savePrice",
                BatteryPrice.class
        );
            assertEquals(user.getPrice(), priceToCreate.getPrice());
    }
    @Test
    void getPrices(){

        Mockito.when(priceService.getAll()).thenReturn(batteryPrices);
        GraphQLQueryRequest graphQLQueryRequest = new GraphQLQueryRequest(
                PricesGraphQLQuery.newRequest().build(),
                new PricesProjectionRoot().price().id());

        List<BatteryPrice> batteries = dgsQueryExecutor.executeAndExtractJsonPath(
                graphQLQueryRequest.serialize(), "data.prices");
        assertEquals(batteries.size(), batteries.size());










    }
}
