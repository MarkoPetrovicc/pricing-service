package com.example.messageserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatteryStatisticDto {
    private List<String> name;
    private double totalWattCapacity;
    private double averageWattCapacity;
}
