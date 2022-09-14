package com.example.messageserver.service;


import com.example.messageserver.model.BatteryOnlyPriceDto;
import com.example.messageserver.model.BatteryPrice;
import com.example.messageserver.model.BatteryStatisticDto;
import com.example.messageserver.model.graphql.types.BatteryPriceInput;
import com.example.messageserver.repository.PriceDALimpl;
import com.example.messageserver.repository.PriceRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;


@Service
@Data
@AllArgsConstructor
public class PriceService {

    private final WebClient webClient;
    private final PriceRepository priceRepository;
    private final PriceDALimpl priceDALimpl;

    private final ModelMapper modelMapper;

    public void calculatePrice(BatteryStatisticDto batteries) {
        BatteryPrice batteryPrice = new BatteryPrice();
        batteryPrice.setPrice(batteries.getTotalWattCapacity()*15);
        batteryPrice.setBatteryName(batteries.getName());
        priceRepository.save(batteryPrice);

        System.out.println("Price is " + batteries.getTotalWattCapacity() * 15 + " RSD" + "for " + batteries.getName());


    }
    public List<BatteryPrice> getAll(){
        return priceRepository.findAll();
    }

    public BatteryPrice findOneByPrice(double price){
        return priceDALimpl.findOneByPrice(price);
    }

    public BatteryOnlyPriceDto getPrice(){
        return webClient.get()
                .uri("/batteryPrice")
                .retrieve()
                .bodyToMono(BatteryOnlyPriceDto.class)
                .block();
    }
    public BatteryPrice savePrice(BatteryPriceInput batteryPriceInput){
        BatteryPrice batteryPrice = modelMapper.map(batteryPriceInput, BatteryPrice.class);
        return priceRepository.save(batteryPrice);
    }
}
