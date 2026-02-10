package com.travel.travel_service.repository;

import com.travel.travel_service.model.TravelConnection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelConnectionRepository
        extends JpaRepository<TravelConnection, Long> {
}
