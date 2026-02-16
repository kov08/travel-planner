package com.travel.travel_service.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GraphVersionService {

    private static final String GRAPH_VERSION_KEY = "graph:version";
    private static final Logger log = LoggerFactory.getLogger(GraphVersionService.class);

    private final RedisTemplate<String, Object> redisTemplate;

    public GraphVersionService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public long getCurrentVersion() {
        Object version = redisTemplate.opsForValue().get(GRAPH_VERSION_KEY);
        if (version == null) {
        	 log.info("Graph version not found in Redis. Initializing to 1.");
        	redisTemplate.opsForValue().set(GRAPH_VERSION_KEY, 1L);
            return 1L;
        }
        
        log.info("Graph version fetched from Redis.");
        
        // Safe conversion using Number
        if (version instanceof Number) {
            return ((Number) version).longValue();
        }
        
        // If Redis somehow returned a String
        if (version instanceof String) {
            return Long.parseLong((String) version);
        }
        
        throw new IllegalStateException("Unexpected type for graph version: " + version.getClass());
    }
   
    public void incrementVersion() {
        redisTemplate.opsForValue().increment(GRAPH_VERSION_KEY);
    }
}