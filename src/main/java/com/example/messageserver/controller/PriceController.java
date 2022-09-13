package com.example.messageserver.controller;

import com.example.messageserver.model.BatteryOnlyPriceDto;
import com.example.messageserver.model.BatteryPrice;
import com.example.messageserver.service.PriceService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/price")
@Data
public class PriceController {

    private final PriceService priceService;

    @GetMapping
    public ResponseEntity<List<BatteryPrice>> getBatteryPrices(){
        return new ResponseEntity<>(priceService.getAll(), HttpStatus.OK);
    }
    @GetMapping("/findOne")
    public ResponseEntity<BatteryPrice> getOneByPrice(@RequestParam double price){
        return new ResponseEntity<>(priceService.findOneByPrice(price), HttpStatus.OK);
    }

    @GetMapping("/apiCall")
    public ResponseEntity<BatteryOnlyPriceDto> getPrice(){
        return new ResponseEntity<>(priceService.getPrice(), HttpStatus.OK);
    }
}
