# Hotel Service - Oteller Microservices Project

The **Hotel Service** is part of the *Oteller Microservices Architecture*.
It is responsible for managing hotel-related operations such as adding hotels, managing rooms, and checking
availability.

## Features

- Register new hotels
- Add rooms to hotels
- Check room availability
- Room validation with custom annotations
- Kafka integration for event-based communication (e.g., room reservation events)
- RESTful API with Spring Boot
- Exception handling with meaningful responses
- Unit with JUnit and Mockito

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
- Kafka (for full functionality, including event production)

### Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/oteller-microservices.git
   cd hotelservice
   ```

2. Configure the database connection in `src/main/resources/application.yml`:

   ```yaml
   spring:
      datasource:
      url: jdbc:postgresql://localhost:5432/hoteldb
      username: postgres
      password: yourpassword
      jpa:
         hibernate:
            ddl-auto: update
         show-sql: true
         properties:
            hibernate.dialect: org.hibernate.dialect.PostgreSQLDialect
   
   kafka:
     bootstrap-servers: localhost:9092
     topic:
       room-reserved: room-reserved-events
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

    3. Create a new token (e.g., hotel-service)

    4. Copy the token and configure the following file in your project root:
       ```yaml
          sonar-project.properties  
       ```

       ```yaml
          sonar.projectKey=hotel-service
          sonar.projectName=hotel-service
          sonar.sources=src
          sonar.java.binaries=target
          sonar.login=your_generated_token_here
       ```
    5. Run the Scanner:
       ```bash
       sonar-scanner
       ```
