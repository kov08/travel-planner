package com.travel.travel_service.service;

import com.travel.travel_service.dto.OptimalPathResult;
import com.travel.travel_service.model.TravelConnection;
import com.travel.travel_service.repository.TravelConnectionRepository;
import com.travel.travel_service.util.DijkstraUtil;
import com.travel.travel_service.util.Edge;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;

@Service
public class TravelPathService {

    private final TravelConnectionRepository repository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final GraphVersionService graphVersionService;
    
    // Set time to live in redis chache
    private static final Duration CACHE_TTL = Duration.ofMinutes(5); 
    
    // log 
    private static final Logger log = LoggerFactory.getLogger(TravelPathService.class);

    public TravelPathService(
    		TravelConnectionRepository repository,   
    		RedisTemplate<String, Object> redisTemplate,
            GraphVersionService graphVersionService) {
        this.repository = repository;
        this.redisTemplate = redisTemplate;
        this.graphVersionService = graphVersionService;
        
    }

    public OptimalPathResult findOptimalPath(
            String source,
            String destination,
            OptimizationCriteria criteria
    ) {
    	long version = graphVersionService.getCurrentVersion();
    	String cacheKey = String.format(
                "route:v%s:%s:%s:%s",
                version, source, destination, criteria
        );
    	
        // 1️ Check Cache
        OptimalPathResult cached =
                (OptimalPathResult) redisTemplate.opsForValue().get(cacheKey);

        if (cached != null) {
        	log.info("Cache HIT - Returning result from Redis for key: {}", cacheKey);
        	//System.out.println("Result from Redis cache");
            return cached;
        }
        
        // 2️ Cache Miss → Build Graph
        log.info("Cache MISS - Fetching data from Database for key: {}", cacheKey);
        List<TravelConnection> connections = repository.findAll();

        Map<String, List<Edge>> graph = new HashMap<>();

        for (TravelConnection c : connections) {
            graph.computeIfAbsent(c.getSource(), k -> new ArrayList<>())
                    .add(new Edge(
                            c.getDestination(),
                            c.getCost(),
                            c.getTime()
                    ));
        }

        OptimalPathResult result =
                DijkstraUtil.findShortestPath(
                        graph, source, destination, criteria
                );

        // 3️ Store in Cache
        redisTemplate.opsForValue().set(
                cacheKey,
                result,
                CACHE_TTL
        );
        
        
        return result;
    }
}