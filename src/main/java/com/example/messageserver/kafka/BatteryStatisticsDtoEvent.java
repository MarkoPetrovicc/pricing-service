package com.example.messageserver.kafka;

import com.example.messageserver.model.BatteryStatisticDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatteryStatisticsDtoEvent {

    private String id;
    private BatteryStatisticDto batteryStatisticDto;
    private BatteryStatisticsDtoOperation batteryStatisticsDtoOperation;
}
