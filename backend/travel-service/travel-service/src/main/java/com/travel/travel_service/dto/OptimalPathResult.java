package com.travel.travel_service.dto;

import java.util.List;

public class OptimalPathResult {

    private final List<String> path;
    private final float totalCost;
    private final float totalTime;

    public OptimalPathResult(List<String> path, float totalCost, float totalTime) {
        this.path = path;
        this.totalCost = totalCost;
        this.totalTime = totalTime;
    }

    public List<String> getPath() {
        return path;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public float getTotalTime() {
        return totalTime;
    }
}