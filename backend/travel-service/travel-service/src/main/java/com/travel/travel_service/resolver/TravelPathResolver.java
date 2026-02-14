package com.travel.travel_service.resolver;

import com.travel.travel_service.dto.OptimalPathResult;
import com.travel.travel_service.service.OptimizationCriteria;
import com.travel.travel_service.service.TravelPathService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TravelPathResolver {

    private final TravelPathService travelPathService;

    public TravelPathResolver(TravelPathService travelPathService) {
        this.travelPathService = travelPathService;
    }

    @QueryMapping
    public OptimalPathResult findOptimalPath(
            @Argument String source,
            @Argument String destination,
            @Argument OptimizationCriteria criteria
    ) {
        return travelPathService.findOptimalPath(source, destination, criteria);
    }
}