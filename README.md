# Oteller Microservices

Oteller is a distributed hotel reservation system built from independently runnable Spring Boot services. It manages hotels, rooms, reservations, and notifications, with Kafka events connecting the services. AI-enabled features provide hotel information and booking assistance.

## Architecture

| Module | Responsibility | Default address |
|---|---|---|
| `apigateway` | Reactive API entry point; routes hotel and reservation requests | `http://localhost:8080` |
| `hotelservice` | Hotel, room, availability, and review management; hotel assistant and MCP server | `http://localhost:8081` |
| `reservationservice` | Reservations, orders, booking assistant, and hotel information RAG | `http://localhost:8082` |
| `notificationservice` | Consumes reservation-created events and sends confirmation emails | Port `8083` |
| `common-events` | Shared Kafka event payloads and enums | Java library |

The gateway currently routes `/hotel-service/**` to the Hotel Service and `/reservation-service/**` to the Reservation Service, removing each prefix before forwarding. For example:

```text
GET http://localhost:8080/reservation-service/reservation
    -> GET http://localhost:8082/reservation
```

See each module's README for its endpoint details and configuration.

## Technology

- Java 17 and Spring Boot
- Spring Cloud Gateway and WebFlux
- Spring Data JPA / Hibernate and PostgreSQL
- Apache Kafka and Spring Kafka
- Spring AI with OpenAI; pgvector for reservation-service RAG
- Maven, Lombok, and MapStruct
- Docker Compose, JaCoCo, and SonarQube

## Prerequisites

- JDK 17 or later
- Maven (or the Maven Wrapper included in each service module)
- Docker and Docker Compose for local infrastructure
- OpenAI API key for AI-enabled Hotel, Reservation, and Notification features
- SMTP account for Notification Service email delivery

## Start Local Infrastructure

From the repository root, start Kafka, PostgreSQL, and SonarQube:

```bash
docker compose up -d
```

Docker Compose exposes these local services:

| Dependency | Address / default |
|---|---|
| Kafka | `localhost:9092` |
| Hotel PostgreSQL | `localhost:5432`, database `hoteldb` |
| Reservation PostgreSQL with pgvector | `localhost:5433`, database `reservationdb` |
| SonarQube | `http://localhost:9000` |

The database defaults in `docker-compose.yml` are `postgres` / `mysecretpassword`; replace them with secured values for non-local environments.

## Runtime Environment Variables

The Hotel, Reservation, and Notification services resolve secrets from environment variables. Do not put secret values in `application.yml` or commit them to source control.

| Variable | Used by | Purpose |
|---|---|---|
| `OPEN_AI_KEY` | Hotel, Reservation, Notification | OpenAI API key |
| `MAIL_USERNAME` | Notification | SMTP account username |
| `MAIL_PASSWORD` | Notification | SMTP password or provider app password |

When deploying these services to Tomcat, configure the applicable variables in the Tomcat service environment and restart Tomcat. The API Gateway and `common-events` do not require these variables.

For local development, define the needed variables in the shell before starting the service. Example for Windows PowerShell:

```powershell
$env:OPEN_AI_KEY="<your-api-key>"
$env:MAIL_USERNAME="<your-smtp-username>"
$env:MAIL_PASSWORD="<your-smtp-app-password>"
```

Example for macOS/Linux:

```bash
export OPEN_AI_KEY="<your-api-key>"
export MAIL_USERNAME="<your-smtp-username>"
export MAIL_PASSWORD="<your-smtp-app-password>"
```

Only define the variables needed by the service you are running.

## Build and Run

Build all Maven modules from the repository root (requires Maven):

```bash
mvn clean install
```

Start the services individually in separate terminals, after infrastructure is available:

```powershell
# Windows PowerShell, from each service directory
.\mvnw.cmd spring-boot:run
```

```bash
# macOS/Linux, from each service directory
./mvnw spring-boot:run
```

Start `hotelservice`, `reservationservice`, `notificationservice`, and `apigateway` separately as needed. For gateway requests, start the target service too. The gateway's JWT issuer is currently configured as `http://localhost:8080`; set it to the actual identity provider issuer for your environment.

## Event Flow

Kafka topics are configured in the service application configuration:

| Topic | Flow |
|---|---|
| `room-reserved-events` | Hotel Service publishes successful room reservation details; Reservation Service consumes them |
| `reservation-created-events` | Reservation Service publishes a created reservation; Notification Service consumes it and sends an email |
| `room-released-events` | Reservation Service publishes a cancellation; Hotel Service consumes it to release the room |

The event payloads and shared enums are defined in `common-events`.

## Tests

Run tests from an individual service directory:

```powershell
# Windows
.\mvnw.cmd test
```

```bash
# macOS/Linux
./mvnw test
```

JaCoCo reports are generated under each module's `target/site/jacoco/` directory when its report goal runs.

## Modules

```text
oteller-microservices/
├── apigateway/
├── common-events/
├── hotelservice/
├── notificationservice/
├── reservationservice/
├── docker-compose.yml
└── README.md
```
