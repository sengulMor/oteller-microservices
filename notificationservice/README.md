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
   ```

4. (Optional) Run SonarQube for Code Quality Analysis:

   #### Option A: Using Docker
    * it should be runable if you already run docker compose file

    * Then open http://localhost:9000
    * Login with:  
      user: admin  
      passwort: admin  
      you will be prompted to change the password

   #### Option B: Manual Installation
    * Download SonarQube: https://www.sonarsource.com/products/sonarqube/downloads/

    * Extract and run:  
      On Windows: StartSonar.bat  
      On Linux/macOS: sonar.sh start

    * Open http://localhost:9000

   Generate a Token for CLI Scanning
    1. Log in to SonarQube

    2. Go to My Account > Security

    3. Create a new token (e.g., notification-service)

    4. Copy the token and configure the following file in your project root:
       ```yaml
          sonar-project.properties  
       ```

       ```yaml
          sonar.projectKey=notification-service
          sonar.projectName=notification-service
          sonar.sources=src
          sonar.java.binaries=target
          sonar.login=your_generated_token_here
       ```
    5. Run the Scanner:
       ```bash
       sonar-scanner
       ```
