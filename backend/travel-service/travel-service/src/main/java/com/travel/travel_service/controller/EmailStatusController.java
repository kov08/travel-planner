package com.travel.travel_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.travel.travel_service.model.EmailStatus;
import com.travel.travel_service.service.TravelPathService;

@RestController
@RequestMapping("/email")
public class EmailStatusController {

    private final TravelPathService travelPathService;

    public EmailStatusController(TravelPathService service) {
        this.travelPathService = service;
    }

    @PostMapping("/update")
    public void updateStatus(
            @RequestParam String cacheKey,
            @RequestParam EmailStatus status) {

        travelPathService.updateEmailStatus(cacheKey, status);
    }
}