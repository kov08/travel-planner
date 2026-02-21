package com.travel.travel_service.event;

import java.time.LocalDateTime;

public class RouteCalculationEvent {

    private String source;
    private String destination;
    private String criteria;
    private LocalDateTime timestamp;

    public RouteCalculationEvent() {}

    public RouteCalculationEvent(String source, String destination, String criteria) {
        this.source = source;
        this.destination = destination;
        this.criteria = criteria;
        this.timestamp = LocalDateTime.now();
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getCriteria() {
        return criteria;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}