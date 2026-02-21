package com.travel.travel_service.repository;

import com.travel.travel_service.model.RouteEmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteEmailStatusRepository extends JpaRepository<RouteEmailStatus,Long>{
//	Optional<RouteEmailStatus> findByKey(String cachekey);
	Optional<RouteEmailStatus> findByCacheKey(String cacheKey);

}