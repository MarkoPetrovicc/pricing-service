package com.example.messageserver.service;

import com.example.messageserver.model.BatteryOnlyPriceDto;
import com.example.messageserver.model.BatteryPrice;
import com.example.messageserver.model.BatteryStatisticDto;
import com.example.messageserver.repository.PriceDALimpl;
import com.example.messageserver.repository.PriceRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


public class PriceServiceTest {

    @Mock
    private PriceRepository priceRepository;
    private PriceService priceService;

    @Mock
    private PriceDALimpl priceDALimpl;

    @Mock
    private WebClient webClient;

    @Autowired ModelMapper modelMapper;


    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;

    @Mock
    private WebClient.RequestBodySpec requestBodySpecMock;

    @Mock
    private WebClient.ResponseSpec mockResponseSpec;
    @Mock
    private WebClient.RequestHeadersSpec mockRequestHeadersSpec;


    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
        priceService =new PriceService(webClient, priceRepository, priceDALimpl, modelMapper);
        when(webClient.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri("/batteryPrice")).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.headers(any())).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.header(any(), any())).thenReturn(mockRequestHeadersSpec);
        when(mockRequestHeadersSpec.retrieve()).thenReturn(mockResponseSpec);

    }
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
    public void shouldSaveCalculatedPrice(){
        priceService.calculatePrice(batteryStatisticDto5);
        batteryPrice2.setId(new ObjectId("6319b3fdcc03d4d86505b6e0"));
        System.out.println(batteryPrice2);
        verify(priceRepository).save(batteryPrice2);
    }

    @Test
    public void shouldFindAllPrices(){
        priceService.getAll();

        verify(priceRepository).findAll();
    }
    @Test
    public void shouldFindOneByPrice(){
        priceService.findOneByPrice(batteryPrice1.getPrice());

        verify(priceDALimpl).findOneByPrice(12.0);
    }
    @Test
    public void shouldReturnPrice(){
        BatteryOnlyPriceDto batteryOnlyPriceDto = new BatteryOnlyPriceDto();
        batteryOnlyPriceDto.setPrice(200.0);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);
        when(responseSpecMock.bodyToMono(BatteryOnlyPriceDto.class)).thenReturn(
                Mono.just(batteryOnlyPriceDto));
        when(requestHeadersUriSpecMock.retrieve()).thenReturn(responseSpecMock);
        BatteryOnlyPriceDto batteryPrice = priceService.getPrice();
        assertThat(batteryPrice).isEqualTo(batteryOnlyPriceDto);


    }

}
