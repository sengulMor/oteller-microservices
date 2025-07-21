# Gateway-service - Oteller Microservices Project

The **Gateway Service** is part of the *Oteller Microservices Architecture*.
API Gateway acts as a single entry point to the system. It is responsible for routing incoming requests to the
appropriate microservices.

## Features

- routing incoming requests

## Tech Stack

- Java 17
- Spring Boot
- Spring Cloud Gateway
- Maven

## Getting Started

### Prerequisites

- Java 17+
- Maven

### Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/oteller-microservices.git
   cd apigateway
   ```

2. Start the application:
   ```bash
   mvn spring-boot:run
   ```

3. (Optional) Run SonarQube for Code Quality Analysis:

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

    3. Create a new token (e.g., gateway-service)

    4. Copy the token and configure the following file in your project root:
       ```yaml
          sonar-project.properties  
       ```

       ```yaml
          sonar.projectKey=gateway-service
          sonar.projectName=gateway-service
          sonar.sources=src
          sonar.java.binaries=target
          sonar.login=your_generated_token_here
       ```
    5. Run the Scanner:
       ```bash
       sonar-scanner
       ```
