# Hotel Service – Oteller Microservices

The **Hotel Service** manages hotels, rooms, and hotel reviews within the Oteller microservices architecture. It also provides a Spring AI assistant and an MCP tool for room availability queries.

## Features

- Create, retrieve, update, and delete hotels
- Manage a hotel's rooms
- Check and reserve room availability
- Custom validation rules for hotels and rooms
- Kafka events for reserved and released rooms
- Create hotel reviews with AI-based sentiment analysis
- Hotel assistant with Spring AI and tool calling
- MCP server over SSE
- Centralized exception handling
- JUnit 5 and Mockito tests

## Technology Stack

- Java 17
- Spring Boot 3 / Spring Web
- Spring Data JPA and Hibernate
- PostgreSQL
- Apache Kafka and Spring Kafka
- Spring AI and Spring AI OpenAI
- Spring AI MCP Server WebMVC
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

From the repository root, start PostgreSQL, Kafka, and SonarQube with Docker Compose:

```bash
docker compose up -d
```

The default connections are:

| Component | Address |
|---|---|
| Hotel PostgreSQL | `localhost:5432`, database `hoteldb` |
| Kafka | `localhost:9092` |
| SonarQube | `http://localhost:9000` |

The credentials are defined in `docker-compose.yml`: username `postgres`, password `mysecretpassword`.

## Configure and Start the Application

The Hotel Service runs on port `8081` by default. Its `application.yml` resolves the OpenAI key from `OPEN_AI_KEY`:

```yaml
spring:
  ai:
    openai:
      api-key: ${OPEN_AI_KEY}
```

When deployed to Tomcat, configure `OPEN_AI_KEY` as an environment variable in the Tomcat service configuration, then restart Tomcat. Do not put the key directly in `application.yml` or commit it to the repository.

For local development with `spring-boot:run`, set the variable in the shell before starting the service:

```powershell
# Windows PowerShell
$env:OPEN_AI_KEY="<your-api-key>"
```

```bash
# macOS/Linux
export OPEN_AI_KEY="<your-api-key>"
```

Then start the service:

```bash
# Windows
.\mvnw.cmd spring-boot:run

# macOS/Linux
./mvnw spring-boot:run
```

The relevant configuration is in `src/main/resources/application.yml`:

```yaml
server:
  port: 8081

spring:
  ai:
    openai:
      api-key: ${OPEN_AI_KEY}
      chat:
        options:
          model: gpt-4o-mini
  mcp:
    server:
      enabled: true
      name: hotel-service
      transport: sse
  datasource:
    url: jdbc:postgresql://localhost:5432/hoteldb
    username: postgres
    password: mysecretpassword
  kafka:
    bootstrap-servers: localhost:9092
```

## REST Endpoints

### Hotels

| Method | Path | Description |
|---|---|---|
| `POST` | `/hotels` | Create a hotel |
| `GET` | `/hotels` | Retrieve all hotels |
| `GET` | `/hotels/{id}` | Retrieve a hotel by ID |
| `PUT` | `/hotels` | Update a hotel |
| `DELETE` | `/hotels/{id}` | Delete a hotel |

### Rooms

| Method | Path | Description |
|---|---|---|
| `POST` | `/rooms` | Create a room |
| `GET` | `/rooms/{id}` | Retrieve a room by ID |
| `GET` | `/rooms?hotelId={id}` | Retrieve all rooms of a hotel |
| `PUT` | `/rooms` | Update a room |
| `DELETE` | `/rooms/{id}` | Delete a room |
| `POST` | `/rooms/check-availability` | Check availability and reserve a room |

### Reviews and Assistant

| Method | Path | Description |
|---|---|---|
| `POST` | `/reviews` | Save a review and analyze it with Spring AI |
| `POST` | `/assistant` | Send a question to the hotel assistant |

Example request for the hotel assistant:

```bash
curl -X POST http://localhost:8081/assistant \
  -H "Content-Type: application/json" \
  -d '{"message":"Is a room available at hotel 1 from 2026-12-20 to 2026-12-23?"}'
```

The assistant can use the Spring AI tool `checkRoomAvailability`. The hotel ID and ISO date values are validated; the check-out date must be after the check-in date.

## MCP Configuration

The MCP server is enabled in `application.yml` and uses the SSE transport. The server name is `hotel-service`.

## Kafka Events

| Topic configuration | Default name |
|---|---|
| `spring.kafka.topic.room-reserved` | `room-reserved-events` |
| `spring.kafka.topic.room-released` | `room-released-events` |

## Tests

Run all tests:

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

The Maven build also generates a JaCoCo report at `target/site/jacoco/index.html`.

## SonarQube

After starting SonarQube with Docker Compose:

1. Open `http://localhost:9000`.
2. Sign in with `admin / admin` and change the password.
3. Create a token under **My Account → Security**.
4. Add the token to `hotelservice/sonar-project.properties`:

```properties
sonar.projectKey=hotel-service
sonar.projectName=hotel-service
sonar.sources=src
sonar.java.binaries=target
sonar.login=<your-generated-token>
```

5. Run the scanner from the `hotelservice` directory:

```bash
sonar-scanner
```
