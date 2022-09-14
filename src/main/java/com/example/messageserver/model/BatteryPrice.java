package com.example.messageserver.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;


@Data
@Document("price")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatteryPrice {

    @Id
    private ObjectId id;
    private List<String> batteryName;
    private double price;
}
