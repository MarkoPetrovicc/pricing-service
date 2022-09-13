package com.example.messageserver.repository;

import com.example.messageserver.model.BatteryPrice;
import lombok.Data;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;

@Repository
@Data
public class PriceDALimpl implements PriceDAL{

    private final MongoTemplate mongoTemplate;

    @Override
    public BatteryPrice savePrice(BatteryPrice batteryPrice) {
        mongoTemplate.save(batteryPrice);
        return batteryPrice;
    }

    @Override
    public List<BatteryPrice> getAllPrices() {
        return mongoTemplate.findAll(BatteryPrice.class);
    }

    @Override
    public List<BatteryPrice> getAllPricesPaginated(int pageNumber, int pageSize) {
        Query query = new Query();
        query.skip(pageNumber * pageSize);
        query.limit(pageSize);

        return mongoTemplate.find(query, BatteryPrice.class);

    }

    @Override
    public BatteryPrice findOneByPrice(double price) {
        Query query = new Query();
        query.addCriteria(Criteria.where("price").is(price));

        return mongoTemplate.findOne(query, BatteryPrice.class);
    }

    @Override
    public List<BatteryPrice> findByName(String name) {
        return null;
    }

    @Override
    public List<BatteryPrice> findByBirthDateAfter(Date date) {
        return null;
    }

    @Override
    public List<BatteryPrice> findByAgeRange(int lowerBound, int upperBound) {
        return null;
    }

    @Override
    public List<BatteryPrice> findByFavoriteBooks(String favoriteBook) {
        return null;
    }

    @Override
    public BatteryPrice updateOnePrice(BatteryPrice batteryPrice) {
        return null;
    }

    @Override
    public void deleteBatteryPrice(BatteryPrice batteryPrice) {

    }
}
