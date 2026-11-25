# Notification Service – Oteller Microservices

The **Notification Service** consumes reservation-created events from Kafka and sends reservation confirmation emails. It uses Spring AI to generate email content.

## Features

- Consume reservation-created events from Kafka
- Generate AI-assisted reservation confirmation messages
- Send email notifications over SMTP
- Validate incoming events and handle email errors
- JUnit 5 and Mockito tests

## Technology Stack

- Java 17
- Spring Boot 3 / Spring Web
- Apache Kafka and Spring Kafka
- Spring Mail
- Spring AI OpenAI
- Maven
- JaCoCo and SonarQube

## Prerequisites

- JDK 17 or later
- Apache Kafka
- SMTP account for sending email
- For AI-generated email content: a valid OpenAI API key
- Optional: Docker and Docker Compose

## Start the Infrastructure

From the repository root, start the shared infrastructure:

```bash
docker compose up -d
```

The Notification Service connects to Kafka at `localhost:9092`. It consumes the `reservation-created-events` topic.

## Configure and Start the Application

The service runs on port `8083` by default. Its `application.yml` reads the SMTP credentials and OpenAI key from environment variables:

```yaml
spring:
  mail:
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
  ai:
    openai:
      api-key: ${OPEN_AI_KEY}
```

For Tomcat deployments, configure `MAIL_USERNAME`, `MAIL_PASSWORD`, and `OPEN_AI_KEY` in the Tomcat service's environment, then restart Tomcat for the changes to take effect. Keep these values out of `application.yml` and source control.

For local development with `spring-boot:run`, define the variables in the shell before starting the service:

```powershell
# Windows PowerShell
$env:MAIL_USERNAME="<your-smtp-username>"
$env:MAIL_PASSWORD="<your-smtp-app-password>"
$env:OPEN_AI_KEY="<your-api-key>"
```

```bash
# macOS/Linux
export MAIL_USERNAME="<your-smtp-username>"
export MAIL_PASSWORD="<your-smtp-app-password>"
export OPEN_AI_KEY="<your-api-key>"
```

### Run the service

```bash
# Windows
.\mvnw.cmd spring-boot:run

# macOS/Linux
./mvnw spring-boot:run
```

## Kafka

| Setting | Default |
|---|---|
| Bootstrap server | `localhost:9092` |
| Topic | `reservation-created-events` |
| Consumer group | `notification-group` |

When a valid reservation-created event is received, the service generates an AI-assisted message and sends it to the guest using the configured SMTP account.

## Tests

Run all tests:

```bash
# Windows
.\mvnw.cmd test

# macOS/Linux
./mvnw test
```

## SonarQube

After starting SonarQube with Docker Compose:

1. Open `http://localhost:9000`.
2. Sign in with `admin / admin` and change the initial password.
3. Create a token under **My Account → Security**.
4. Configure the token in `sonar-project.properties`:

```properties
sonar.projectKey=notification-service
sonar.projectName=Notification Service
sonar.sources=src
sonar.java.binaries=target
sonar.login=<your-generated-token>
```

5. Run the scanner from the `notificationservice` directory:

```bash
sonar-scanner
```
