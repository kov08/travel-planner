package com.travel.travel_service.util;

public class Edge {

    public final String to;
    public final float cost;
    public final float time;

    public Edge(String to, float cost, float time) {
        this.to = to;
        this.cost = cost;
        this.time = time;
    }
}