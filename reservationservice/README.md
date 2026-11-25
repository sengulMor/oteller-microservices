# Reservation Service – Oteller Microservices

The **Reservation Service** manages hotel reservations, orders, Kafka event integration, and AI-assisted booking and hotel information queries within the Oteller microservices architecture.

## Features

- Create, retrieve, list, and cancel reservations
- Validate check-in/check-out date ranges
- Create simple orders
- Consume room-reserved events and create reservations transactionally
- Publish reservation-created and room-released events
- Provide a RAG hotel assistant using PostgreSQL pgvector
- Provide a booking assistant with Spring AI tool calling
- Centralized exception handling
- JUnit 5 and Mockito tests

## Technology Stack

- Java 17
- Spring Boot 3 / Spring Web
- Spring Data JPA and Hibernate
- PostgreSQL with pgvector
- Apache Kafka and Spring Kafka
- Spring AI OpenAI and Vector Store
- Spring Security
- Lombok
- MapStruct
- Maven
- Docker Compose
- JaCoCo and SonarQube

## Prerequisites

- JDK 17 or later
- PostgreSQL
- Apache Kafka
- Optional: Docker and Docker Compose
- For AI features: a valid OpenAI API key

## Start the Infrastructure

From the repository root:

```bash
docker compose up -d
```

The Reservation Service uses:

| Component | Address |
|---|---|
| Reservation PostgreSQL/pgvector | `localhost:5433`, database `reservationdb` |
| Kafka | `localhost:9092` |
| Hotel Service | `http://localhost:8081` |
| SonarQube | `http://localhost:9000` |

The PostgreSQL credentials are `postgres` / `mysecretpassword`.

## Configure and Start the Application

The `application.yml` configuration resolves the OpenAI key from `OPEN_AI_KEY`:

```yaml
spring:
  ai:
    openai:
      api-key: ${OPEN_AI_KEY}
```

When deployed to Tomcat, configure `OPEN_AI_KEY` as an environment variable in the Tomcat service configuration, then restart Tomcat. Do not put the key directly in `application.yml` or commit it to the repository.

For local development with `spring-boot:run`, set the variable in the shell before starting the service:

```bash
# Windows PowerShell
$env:OPEN_AI_KEY="<your-api-key>"
```

```bash
# macOS/Linux
export OPEN_AI_KEY="<your-api-key>"
```

Start the service:

```bash
# Windows
.\mvnw.cmd spring-boot:run

# macOS/Linux
./mvnw spring-boot:run
```

The service runs on port `8082` by default. Its main configuration is in `src/main/resources/application.yml`:

```yaml
server:
  port: 8082

spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/reservationdb
    username: postgres
    password: mysecretpassword
  ai:
    openai:
      api-key: ${OPEN_AI_KEY}
      chat:
        options:
          model: gpt-4o-mini
  kafka:
    topic:
      room-reserved: room-reserved-events
      reservation-created: reservation-created-events
      room-released: room-released-events

hotel-service:
  base-url: http://localhost:8081
```

All HTTP endpoints are currently allowed by the service security configuration.

## REST Endpoints

### Reservations

| Method | Path | Description |
|---|---|---|
| `POST` | `/reservation` | Create a reservation |
| `GET` | `/reservation` | Retrieve all reservations |
| `GET` | `/reservation/{id}` | Retrieve a reservation by ID |
| `DELETE` | `/reservation/{id}` | Cancel a reservation |
| `GET` | `/reservation/ask?question={text}` | Ask the RAG hotel assistant |

Example reservation request:

```bash
curl -X POST http://localhost:8082/reservation \
  -H "Content-Type: application/json" \
  -d '{
    "hotelId": 1,
    "roomId": 10,
    "guestName": "Ada Lovelace",
    "checkInDate": "2026-12-20",
    "checkOutDate": "2026-12-23",
    "email": "ada@example.com"
  }'
```

### Orders

| Method | Path | Description |
|---|---|---|
| `POST` | `/order` | Create an order |

```bash
curl -X POST http://localhost:8082/order \
  -H "Content-Type: application/json" \
  -d '{"name":"Airport transfer","description":"Pickup at 15:00"}'
```

### Booking Assistant

| Method | Path | Description |
|---|---|---|
| `GET` | `/booking-assistant/chat?message={text}` | Chat with the Spring AI booking assistant |

```bash
curl "http://localhost:8082/booking-assistant/chat?message=Is%20room%2010%20available%20from%202026-12-20%20to%202026-12-23"
```

The assistant can use the `RoomAvailabilityTools` Spring AI tool and validates hotel ID, room availability parameters, and date ranges before calling downstream services.

## Kafka Event Flow

| Topic | Default name | Role |
|---|---|---|
| `spring.kafka.topic.room-reserved` | `room-reserved-events` | Consumed after a room reservation succeeds |
| `spring.kafka.topic.reservation-created` | `reservation-created-events` | Published after the reservation is created |
| `spring.kafka.topic.room-released` | `room-released-events` | Published when a reservation is cancelled |

The room-reserved listener validates the event and creates the reservation transactionally. Cancellation publishes a room-released event so the Hotel Service can release the room.

## Tests

Run all tests:

```bash
# Windows
.\mvnw.cmd test

# macOS/Linux
./mvnw test
```

The Maven build generates a JaCoCo coverage report at `target/site/jacoco/index.html`.

## SonarQube

After starting SonarQube:

1. Open `http://localhost:9000`.
2. Sign in with `admin / admin` and change the initial password.
3. Create a token under **My Account → Security**.
4. Update `sonar-project.properties`:

```properties
sonar.projectKey=reservation-service
sonar.projectName=Reservation Service
sonar.sources=src
sonar.java.binaries=target
sonar.login=<your-generated-token>
```

5. Run the scanner from the `reservationservice` directory:

```bash
sonar-scanner
```
