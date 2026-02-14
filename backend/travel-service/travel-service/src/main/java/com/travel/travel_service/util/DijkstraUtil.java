package com.travel.travel_service.util;

import com.travel.travel_service.dto.OptimalPathResult;
import com.travel.travel_service.service.OptimizationCriteria;

import java.util.*;

public final class DijkstraUtil {

    private DijkstraUtil() {} // Prevent instantiation

    public static OptimalPathResult findShortestPath(
            Map<String, List<Edge>> graph,
            String source,
            String destination,
            OptimizationCriteria criteria
    ) {
        Map<String, Float> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();

        PriorityQueue<String> pq =
                new PriorityQueue<>(Comparator.comparing(distances::get));

        for (String node : graph.keySet()) {
            distances.put(node, Float.MAX_VALUE);
        }

        distances.put(source, 0f);
        pq.add(source);

        while (!pq.isEmpty()) {
            String current = pq.poll();

            if (current.equals(destination)) break;

            for (Edge edge : graph.getOrDefault(current, List.of())) {

                float weight =
                        criteria == OptimizationCriteria.COST
                                ? edge.cost
                                : edge.time;

                float newDist = distances.get(current) + weight;

                if (newDist < distances.getOrDefault(edge.to, Float.MAX_VALUE)) {
                    distances.put(edge.to, newDist);
                    previous.put(edge.to, current);
                    pq.add(edge.to);
                }
            }
        }

        // Reconstruct path
        List<String> path = new ArrayList<>();
        String step = destination;

        while (step != null) {
            path.add(step);
            step = previous.get(step);
        }

        Collections.reverse(path);

        float totalCost = 0f;
        float totalTime = 0f;

        for (int i = 0; i < path.size() - 1; i++) {
            for (Edge edge : graph.get(path.get(i))) {
                if (edge.to.equals(path.get(i + 1))) {
                    totalCost += edge.cost;
                    totalTime += edge.time;
                }
            }
        }

        return new OptimalPathResult(path, totalCost, totalTime);
    }
}