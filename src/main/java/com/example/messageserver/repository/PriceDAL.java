package com.example.messageserver.repository;

import com.example.messageserver.model.BatteryPrice;

import java.util.Date;
import java.util.List;

public interface PriceDAL {

        BatteryPrice savePrice(BatteryPrice batteryPrice);
        List<BatteryPrice> getAllPrices();
        List<BatteryPrice> getAllPricesPaginated(
                int pageNumber, int pageSize);
        BatteryPrice findOneByPrice(double price);
        List<BatteryPrice> findByName(String name);
        List<BatteryPrice> findByBirthDateAfter(Date date);
        List<BatteryPrice> findByAgeRange(int lowerBound, int upperBound);
        List<BatteryPrice> findByFavoriteBooks(String favoriteBook);
        BatteryPrice updateOnePrice(BatteryPrice batteryPrice);
        void deleteBatteryPrice(BatteryPrice batteryPrice);

}
