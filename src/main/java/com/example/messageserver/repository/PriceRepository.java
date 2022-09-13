package com.example.messageserver.repository;

import com.example.messageserver.model.BatteryPrice;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends MongoRepository<BatteryPrice, ObjectId> {

}
