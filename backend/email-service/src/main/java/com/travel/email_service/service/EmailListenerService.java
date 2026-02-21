package com.travel.email_service.service;

import com.travel.email_service.event.RouteCalculationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailListenerService {

    private static final Logger log =
            LoggerFactory.getLogger(EmailListenerService.class);

    @KafkaListener(
            topics = "route.calculation.started",
            groupId = "email-service-group"
    )
    public void handleRouteEvent(@Header(KafkaHeaders.RECEIVED_KEY) String cacheKey, RouteCalculationEvent event) {
    	
        log.info("Received Kafka Event: {}", event);

        try {
        	Thread.sleep(2000);
        	
        	RestTemplate restTemplate = new RestTemplate();
        	restTemplate.postForObject(
        			"http://localhost:8080/email/update?cacheKey=" +cacheKey + "&status=SENT", 
                    null, 
                    void.class
        			);
        } catch(Exception e) {
        	log.error("Email failed");
        }
        
        
        System.out.println("====================================");
        System.out.println("📩 EMAIL SERVICE TRIGGERED");
        System.out.println("Share the route with user");
        System.out.println("Route: " + event.getSource()
                + " -> " + event.getDestination());
        System.out.println("====================================");
    }
}