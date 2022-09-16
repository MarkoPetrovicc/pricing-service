package com.example.messageserver.kafka;

import com.example.messageserver.model.BatteryStatisticDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatteryStatisticsDtoEvent {

    private UUID eventUid;
    private BatteryStatisticDto batteryStatisticDto;
    private BatteryStatisticsDtoOperation batteryStatisticsDtoOperation;
}
