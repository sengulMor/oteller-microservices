# Notification Service - Oteller Microservices Project

The **Notification Service** is part of the *Oteller Microservices Architecture*. It listens for Kafka events (e.g.,
`reservation-created-events`) and simulates sending notification emails to customers who reserve hotel rooms.

## Features

- Kafka integration for event-driven communication
- Email notification simulation
- Robust exception handling with meaningful error messages
- Unit testing with JUnit and Mockito

## Tech Stack

- Java 17
- Spring Boot
- Apache Kafka
- Maven

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Kafka (can be run via Docker or installed locally)

### Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/oteller-microservices.git
   cd notificationservice
   ```

2. Configure the email user in `src/main/resources/application.yml`:

   ```yaml
   spring:
     kafka:
       topic:
         reservation-created: reservation-created-events
       bootstrap-servers: localhost:9092
     mail:
       host: smtp.gmail.com
       port: 587
       username: fakemail@gmail.com
       password: 
       properties:
         mail:
           smtp:
             auth: true
             starttls:
               enable: true
             ssl:
               trust: smtp.gmail.com
   ```

3. Start the application:
   ```bash
   mvn spring-boot:run
