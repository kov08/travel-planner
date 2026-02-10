package com.travel.travel_service.service;

import com.travel.travel_service.model.TravelConnection;
import com.travel.travel_service.repository.TravelConnectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelService {

    private final TravelConnectionRepository repository;

    public TravelService(TravelConnectionRepository repository) {
        this.repository = repository;
    }

    public TravelConnection addConnection(
            String source,
            String destination,
            float cost,
            float time,
            String mode
    ) {
        TravelConnection connection =
                new TravelConnection(source, destination, cost, time, mode);
        System.out.println("Reached at the TravelService");
        return repository.save(connection);
    }

    public List<TravelConnection> getAllConnections() {
        return repository.findAll();
    }

    public void deleteConnection(Long id) {
        repository.deleteById(id);
    }
}
