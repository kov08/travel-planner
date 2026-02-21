package com.travel.travel_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "route_email_status")
public class RouteEmailStatus {
	
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String cacheKey;
	
	@Enumerated(EnumType.STRING)
	private EmailStatus emailStatus;
	
	public RouteEmailStatus() {};
	
	public RouteEmailStatus(String cacheKey, EmailStatus emailStatus) {
		this.cacheKey = cacheKey;
		this.emailStatus = emailStatus;
	}

	public Long getId() {
		return id;
	}

	public String getCacheKey() {
		return cacheKey;
	}

	public EmailStatus getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(EmailStatus emailStatus) {
		this.emailStatus = emailStatus;
	}
}
