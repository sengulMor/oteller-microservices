# Oteller Microservices Project

**Oteller Microservices** is a distributed hotel reservation system composed of several independently deployable
services. It allows hotel registration, room management, and room reservation, and uses Kafka for asynchronous
communication between services.

## Microservices

The system includes the following services:

- **API Gateway** – Central entry point for routing requests to services
- **Hotel Service** – Manages hotels and their rooms
- **Reservation Service** – Handles reservation creation and lookup
- **Notification Service** – Listens for reservation events and sends notifications

Additionally, there's a shared library:

- **common-events** – Contains reusable DTOs and event classes for Kafka communication

## Features

- Register new hotels
- Add and manage hotel rooms
- Check room availability
- Create and manage reservations
- Kafka-based event-driven communication
- Room and reservation validation using custom annotations
- RESTful APIs using Spring Boot
- Exception handling with meaningful responses
- Unit testing with JUnit and Mockito

## Tech Stack

- Java 17
- Spring Boot
- Maven
- Apache Kafka
- PostgreSQL
- JPA / Hibernate
- Docker (optional for local deployment)
- Lombok
- MapStruct

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL
- Kafka (for asynchronous messaging)
- Docker (optional, for running PostgreSQL/Kafka locally)

### Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/oteller-microservices.git
   cd oteller-microservices
   ```
2. Navigate to a service directory (e.g., `cd reservationservice`), configure `application.yml`, and run:
   ```bash
   mvn spring-boot:run
   ```

3. Use Docker Compose to start SonarQube, Kafka, and PostgreSQL:
   ```bash
   docker-compose up -d
   ```

Each service runs independently. Ensure the dependent services (like Kafka or PostgreSQL) are running before starting
services that rely on them.

## Project Structure

<pre>  
      oteller-microservices/ 
      ├── apigateway/ 
      ├── hotelservice/ 
      ├── reservationservice/ 
      ├── notificationservice/ 
      ├── common-events/ 
      ├── docker-compose.yml 
      └── README.md 
</pre>

## Future Improvements

- Authentication and authorization (e.g., Keycloak or OAuth2)
- API documentation with Swagger / OpenAPI
- Centralized configuration (e.g., Spring Cloud Config)
- Tracing and monitoring (e.g., Zipkin, Prometheus, Grafana)

