package com.travel.travel_service.service;

import com.travel.travel_service.dto.OptimalPathResult;
import com.travel.travel_service.model.EmailStatus;
import com.travel.travel_service.model.RouteEmailStatus;
import com.travel.travel_service.model.TravelConnection;
import com.travel.travel_service.repository.RouteEmailStatusRepository;
import com.travel.travel_service.repository.TravelConnectionRepository;
import com.travel.travel_service.util.DijkstraUtil;
import com.travel.travel_service.util.Edge;
import com.travel.travel_service.event.RouteCalculationEvent;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Duration;
import java.util.*;

@Service
public class TravelPathService {

    private final TravelConnectionRepository repository;
    private final RouteEmailStatusRepository emailRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final GraphVersionService graphVersionService;
    private final KafkaTemplate<String, RouteCalculationEvent> kafkaTemplate;
    
    // Set time to live redis cache
    private static final Duration CACHE_TTL = Duration.ofMinutes(5); 
    
    // log 
    private static final Logger log = LoggerFactory.getLogger(TravelPathService.class);

    public TravelPathService(
    		TravelConnectionRepository repository,   
    		RouteEmailStatusRepository emailRepo,
    		SimpMessagingTemplate messagingTemplate,
    		RedisTemplate<String, Object> redisTemplate,
            GraphVersionService graphVersionService,
            KafkaTemplate<String, RouteCalculationEvent> kafkaTemplate) {
        this.repository = repository;
		this.emailRepo = emailRepo;
		this.messagingTemplate = messagingTemplate;
        this.redisTemplate = redisTemplate;
        this.graphVersionService = graphVersionService;
        this.kafkaTemplate = kafkaTemplate;    
    }

    public OptimalPathResult findOptimalPath(
            String source,
            String destination,
            OptimizationCriteria criteria
    ) {
    	
    	// Emit Kafka event immediately
    	RouteCalculationEvent event = new RouteCalculationEvent(source, destination, criteria.name());
    	
    	long version = graphVersionService.getCurrentVersion();
    	
    	String cacheKey = String.format(
                "route:v%s:%s:%s:%s",
                version, source, destination, criteria
        );
    	
    	emailRepo.save(
    			new RouteEmailStatus(cacheKey, EmailStatus.PENDING)
    	);
    	
    	
    	kafkaTemplate.send("route.calculation.started",cacheKey, event);
    	log.info("Kafka Event Sent: route.calculation.started for {} -> {} and CacheKey: {}", source, destination, cacheKey);
    	
    	
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
        
        // Graph Preparation
        Map<String, List<Edge>> graph = new HashMap<>();

        for (TravelConnection c : connections) {
            graph.computeIfAbsent(c.getSource(), k -> new ArrayList<>())
                    .add(new Edge(
                            c.getDestination(),
                            c.getCost(),
                            c.getTime()
                    ));
        }
        
        //Find optimal path using Dijkstra's Algorithm
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
    
    // Called by EMAIL Service
    public void updateEmailStatus(String cacheKey, EmailStatus status) {
    	
    	RouteEmailStatus entity = emailRepo.findByCacheKey(cacheKey).orElseThrow();
    	entity.setEmailStatus(status);
    	emailRepo.save(entity);
    	
    	// PushwebSOcket Message 
    	messagingTemplate.convertAndSend("/topic/email-status", cacheKey+" : "+status);
    	log.info("Websocket pushes for {}", cacheKey);
    }
    
}