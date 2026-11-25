# API Gateway – Oteller Microservices

The **API Gateway** is the system's reactive entry point. It routes requests to the Hotel Service and Reservation Service and removes the gateway-specific URL prefix before forwarding each request.

## Features

- Route hotel requests to the Hotel Service
- Route reservation requests to the Reservation Service
- Spring Cloud Gateway with WebFlux
- OAuth2 resource-server/JWT configuration
- Actuator dependency for application monitoring

## Technology Stack

- Java 17
- Spring Boot
- Spring Cloud Gateway
- Spring WebFlux
- Spring Security OAuth2 Resource Server
- Maven
- JaCoCo

## Prerequisites

- JDK 17 or later
- Maven, or the included Maven Wrapper
- Hotel Service and/or Reservation Service running for proxied requests
- An OAuth2 issuer if JWT validation is enabled in the deployment

## Configuration and Startup

The gateway listens on port `8080` by default. Routes and the JWT issuer are configured in `src/main/resources/application.yml`.

| Gateway path | Forwarded service | Path forwarded to service |
|---|---|---|
| `/hotel-service/**` | `http://localhost:8081` | Prefix `/hotel-service` is removed |
| `/reservation-service/**` | `http://localhost:8082` | Prefix `/reservation-service` is removed |

For example, `GET http://localhost:8080/reservation-service/reservation` is forwarded to `GET http://localhost:8082/reservation`.

The current JWT issuer URI is `http://localhost:8080`. Set it to the actual identity provider's issuer URI for your environment. The API Gateway itself does not use `OPEN_AI_KEY`; configure that variable on the AI-enabled Hotel, Reservation, and Notification services instead.

Run the gateway from this directory:

```bash
# Windows
.\mvnw.cmd spring-boot:run

# macOS/Linux
./mvnw spring-boot:run
```

## Tests

```bash
# Windows
.\mvnw.cmd test

# macOS/Linux
./mvnw test
```

## SonarQube

The repository Docker Compose setup includes SonarQube at `http://localhost:9000`. Create a SonarQube token and configure it in `sonar-project.properties`, then run `sonar-scanner` from the `apigateway` directory.
