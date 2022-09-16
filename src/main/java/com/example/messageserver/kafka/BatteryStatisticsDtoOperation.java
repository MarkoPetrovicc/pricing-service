package com.example.messageserver.kafka;

public enum BatteryStatisticsDtoOperation {

    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH");

    private String value;

    private BatteryStatisticsDtoOperation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}