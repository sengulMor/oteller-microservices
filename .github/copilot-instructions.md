# Oteller Project - GitHub Copilot Instructions

## 1. Project Context & Architecture
- **Architecture**: Distributed Java Microservices with Spring Boot 3.x and Java 17+.
- **Services**: `hotelservice`, `reservationservice`, `notificationservice`, and `common-events` (shared library).
- **AI Integration**: Spring AI (`ChatClient`, `@Tool`, RAG via PostgreSQL `pgvector`, MCP).
- **Database & Messaging**: PostgreSQL, Spring Data JPA / Hibernate, Apache Kafka.

## 2. Java & Spring Boot Guidelines
- Use Java 17+ features (Records, Pattern Matching, Sealed Classes where appropriate).
- Strict Architecture Layering: `Controller -> Service -> Repository / Tools`.
- DTOs and Kafka Events must reside in or extend patterns from `common-events` where shared across services.
- Prefer Constructor Injection over `@Autowired` on fields.
- Controllers should remain thin and only handle HTTP Request/Response mapping.

## 3. JPA & Database Rules
- Use `@Transactional(readOnly = true)` for all read-only Service/Repository operations.
- Avoid N+1 query problems explicitly (use `JOIN FETCH`, projections, or targeted JPQL queries instead of eager fetching).
- Maintain clean repository method naming adhering to Spring Data JPA standards (e.g., `existsByAvailableAndHotel_Id`).
- Use MapStruct for DTO-to-Entity and Entity-to-DTO mappings.

## 4. Spring AI & Tool Calling Standards
- Define Spring AI tools using `@Tool` annotations on dedicated Service components (e.g., `HotelAvailabilityTools`).
- Every `@Tool` and `@ToolParam` must include clear, descriptive text detailing intent and format (e.g., dates in `YYYY-MM-DD`).
- Always validate tool parameters before passing them downstream.

## 5. Testing & Code Quality
- Provide JUnit 5 and Mockito unit tests for new Service classes, Tools, and Controllers.
- Assert edge cases (e.g., `null` parameters, empty DB results, invalid date ranges).
- Follow clean code practices: descriptive variable names, minimal duplication, and clear exception handling.