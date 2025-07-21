# Reservation Service - Oteller Microservices Project

The **Reservation Service** is part of the *Oteller Microservices Architecture*. It handles all reservation-related
operations such as creating, retrieving, and validating hotel reservations. The service also produces Kafka events (
e.g., `reservation-created`) to notify other components of the system.

## Features

- Create new reservations
- Retrieve all reservations
- Retrieve a reservation by ID
- Input validation using custom annotations
- Kafka integration for event publishing (`reservation-created`)
- RESTful API using Spring Boot
- Exception handling with detailed responses
- Unit testing with JUnit and Mockito

## Tech Stack

- Java 17
- Spring Boot
- Maven
- Kafka (for asynchronous event communication)
- JPA/Hibernate
- PostgreSQL
- Docker (optional)
- Lombok
- MapStruct

## Getting Started

### Prerequisites

- Java 17+
- Maven
- PostgreSQL (or use Docker to spin up a container)
- Kafka (can be run via Docker or installed locally)

### Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/oteller-microservices.git
   cd reservationservice
   ```

2. Configure the database connection in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/reservationdb
    username: postgres
    password: yourpassword
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

kafka:
  bootstrap-servers: localhost:9092
  topic:
    room-reserved: room-reserved-events
    reservation-created: reservation-created-events
```

3. Start the application:
   ```bash
   mvn spring-boot:run

