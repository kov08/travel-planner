package com.travel.travel_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "travel_connection")
public class TravelConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String destination;

    private float cost;
    private float time;
    private String mode;

    public TravelConnection() {}

    public TravelConnection(String source, String destination, float cost, float time, String mode) {
        this.source = source;
        this.destination = destination;
        this.cost = cost;
        this.time = time;
        this.mode = mode;
    }

    // getters & setters
    public Long getId() { return id; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public float getCost() { return cost; }
    public float getTime() { return time; }
    public String getMode() { return mode; }

    public void setId(Long id) { this.id = id; }
    public void setSource(String source) { this.source = source; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setCost(float cost) { this.cost = cost; }
    public void setTime(float time) { this.time = time; }
    public void setMode(String mode) { this.mode = mode; }
}
