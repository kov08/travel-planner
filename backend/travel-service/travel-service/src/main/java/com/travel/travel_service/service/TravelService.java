package com.travel.travel_service.service;

import com.travel.travel_service.model.TravelConnection;
import com.travel.travel_service.repository.TravelConnectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelService {

    private final TravelConnectionRepository repository;
    private final GraphVersionService graphVersionService;

    public TravelService(
    		TravelConnectionRepository repository, 
    		GraphVersionService graphVersionService) {
        this.repository = repository;
        this.graphVersionService = graphVersionService;
    }

    public TravelConnection addConnection(
            String source,
            String destination,
            float cost,
            float time,
            String mode
    ) {
        TravelConnection connection = new TravelConnection(source, destination, cost, time, mode);
        TravelConnection saved = repository.save(connection);
        System.out.println("Reached at the TravelService");
        // IMPORTANT: Invalidate graph cache logically
        graphVersionService.incrementVersion();

        return saved;
    }

    public List<TravelConnection> getAllConnections() {
        return repository.findAll();
    }

    public void deleteConnection(Long id) {
        repository.deleteById(id);    
        // IMPORTANT: Invalidate graph cache logically
        graphVersionService.incrementVersion();
    }
}
