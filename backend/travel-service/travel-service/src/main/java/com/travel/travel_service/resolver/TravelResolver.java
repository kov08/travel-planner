package com.travel.travel_service.resolver;

import com.travel.travel_service.model.TravelConnection;
import com.travel.travel_service.service.TravelService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TravelResolver {

    private final TravelService travelService;

    public TravelResolver(TravelService travelService) {
        this.travelService = travelService;
    }

    @QueryMapping
    public List<TravelConnection> getAllConnections() {
        return travelService.getAllConnections();
    }

    @MutationMapping
    public TravelConnection addConnection( 
            @Argument String source,
            @Argument String destination,
            @Argument float cost,
            @Argument float time,
            @Argument String mode
    ) {
    	System.out.println("Reached at the Resolver");
        return travelService.addConnection(source, destination, cost, time, mode);
    }

    @MutationMapping
    public Boolean deleteConnection(@Argument Long id) {
        travelService.deleteConnection(id);
        return true;
    }


}
