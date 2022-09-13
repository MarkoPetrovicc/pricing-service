package com.example.messageserver.graphql;

import com.example.messageserver.model.BatteryInput;
import com.example.messageserver.model.BatteryPrice;
import com.example.messageserver.repository.PriceRepository;
import com.example.messageserver.service.PriceService;
import com.netflix.graphql.dgs.*;
import lombok.Data;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;

import java.util.List;

@DgsComponent
@Data
public class DataFetcher {

    private final PriceRepository priceRepository;
    private final ModelMapper modelMapper;

    private final PriceService priceService;

    @DgsQuery(field = "prices")
    public List<BatteryPrice> prices(){
        return priceService.getAll();
    }

    @DgsMutation
    public BatteryPrice savePrice(@InputArgument("batteryPriceInput") BatteryInput batteryInput){
        BatteryPrice price = modelMapper.map(batteryInput, BatteryPrice.class);
       return priceService.savePrice(price);

    }


}
