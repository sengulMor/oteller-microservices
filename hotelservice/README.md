# Hotel Service - Oteller Microservices Project

This is the **Hotel Service** of the Oteller Microservices architecture. It is responsible for managing hotel-related operations such as adding hotels, managing rooms, and checking availability.

## Features

- Register new hotels
- Add rooms to hotels
- Check room availability
- Room validation with custom annotations
- Kafka integration for event-based communication (e.g., room reservation events)
- RESTful API with Spring Boot
- Exception handling with meaningful responses
- Unit and integration tests with JUnit and Mockito

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
