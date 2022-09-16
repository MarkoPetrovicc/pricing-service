package com.example.messageserver.service;


import com.example.messageserver.model.BatteryOnlyPriceDto;
import com.example.messageserver.model.BatteryPrice;
import com.example.messageserver.model.BatteryStatisticDto;
import com.example.messageserver.model.graphql.types.BatteryPriceInput;
import com.example.messageserver.repository.PriceDALimpl;
import com.example.messageserver.repository.PriceRepository;
import lombok.*;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;


@Service
@Data
@RequiredArgsConstructor
public class PriceService {

    private final WebClient webClient;
    private final PriceRepository priceRepository;
    private final PriceDALimpl priceDALimpl;

    private final ModelMapper modelMapper;

    @Value("${base.url}")
    private String baseUrl;


    public void calculatePrice(BatteryStatisticDto batteries, String operation) {
        BatteryOnlyPriceDto batteryOnlyPriceDto = getPrice(operation);
        BatteryPrice batteryPrice = new BatteryPrice();
        batteryPrice.setPrice(batteries.getTotalWattCapacity()* batteryOnlyPriceDto.getPrice());
        batteryPrice.setBatteryName(batteries.getName());
        priceRepository.save(batteryPrice);

    }
    public List<BatteryPrice> getAll(){
        return priceRepository.findAll();
    }

    public BatteryPrice findOneByPrice(double price){
        return priceDALimpl.findOneByPrice(price);
    }

    public BatteryOnlyPriceDto getPrice(String operation){
            return webClient.get()
                    .uri(formUri(operation))
                    .retrieve()
                    .bodyToMono(BatteryOnlyPriceDto.class)
                    .block();

    }
    public BatteryPrice savePrice(BatteryPriceInput batteryPriceInput){
        BatteryPrice batteryPrice = modelMapper.map(batteryPriceInput, BatteryPrice.class);
        return priceRepository.save(batteryPrice);
    }
    private String formUri(String operation){
        switch (operation) {

            case "MEDIUM" :
                return baseUrl + "/?operation=MEDIUM";

            case "HIGH":
                return baseUrl + "/operation=HIGH";

            default:
                return baseUrl + "/?operation=LOW";

        }
    }
    public List<BatteryPrice> getAllPrices(){
        return priceRepository.findAll();
    }
}
