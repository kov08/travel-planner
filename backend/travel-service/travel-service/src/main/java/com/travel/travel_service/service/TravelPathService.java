package com.travel.travel_service.service;

import com.travel.travel_service.dto.OptimalPathResult;
import com.travel.travel_service.model.TravelConnection;
import com.travel.travel_service.repository.TravelConnectionRepository;
import com.travel.travel_service.util.DijkstraUtil;
import com.travel.travel_service.util.Edge;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TravelPathService {

    private final TravelConnectionRepository repository;

    public TravelPathService(TravelConnectionRepository repository) {
        this.repository = repository;
    }

    public OptimalPathResult findOptimalPath(
            String source,
            String destination,
            OptimizationCriteria criteria
    ) {
        List<TravelConnection> connections = repository.findAll();

        Map<String, List<Edge>> graph = new HashMap<>();

        for (TravelConnection c : connections) {
            graph
                .computeIfAbsent(c.getSource(), k -> new ArrayList<>())
                .add(new Edge(
                        c.getDestination(),
                        c.getCost(),
                        c.getTime()
                ));
        }

        return DijkstraUtil.findShortestPath(
                graph, source, destination, criteria
        );
    }
}