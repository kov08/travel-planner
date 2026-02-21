# Travel Planner

Microservices-based Travel Planner built with:

- Frontend: React, Apollo Client, GraphQL
- Backend: Java 21, Spring Boot 3, Spring GraphQL, JPA, Hibernate
- Database: PostgreSQL
- Messaging : Apache Kafka
- Cache : Redis
- Containerization: Docker

## Features

- Add and delete travel connections
- GraphQL-based data fetching
- Clean layered architecture
- Dockerized PostgreSQL

## Project Structure

frontend/ → React UI  
backend/ → Spring Boot services

## How to Run

1. Start PostgreSQL using Docker
2. Run backend (Spring Boot)
3. Run frontend (npm start)

## Backend Microservices

- travel-service (GraphQL + Redis + PostgreSQL)
- email-service (Kafka consumer notification service)

Run Order:

1. Start Kafka + Zookeeper
2. Start travel-service (Port 8080)
3. Start email-service (Port 8082)
