# Incident Ticket API

A REST API for managing internal IT incident tickets.

## Project Goal

This project demonstrates backend development with Spring Boot, PostgreSQL, validation, testing, Docker, OpenAPI documentation, and a CI pipeline.

The application is designed as a portfolio project. It provides a REST API for creating, reading, updating, deleting, and filtering internal IT incident tickets.

## Current Status

The basic Spring Boot setup is complete. The application is connected to a local PostgreSQL database. The initial ticket data model has been implemented as a JPA entity with status and priority enums. A Spring Data JPA repository has been added for persistence operations and query methods. Request and response DTOs have been added with validation rules for clean API input handling. A mapper has been implemented to convert between entities and DTOs. The service layer has been implemented to handle ticket business logic. A REST controller has been added to expose ticket operations over HTTP.

Centralized error handling, OpenAPI documentation, Docker Compose, and GitHub Actions are not implemented yet.

## Tech Stack

1. Java 21  
   Main programming language for the backend application.

2. Spring Boot  
   Application framework used to build and run the backend service.

3. Spring Web  
   Used for REST endpoints.

4. Spring Data JPA  
   Used for persistence and repository based database access.

5. Hibernate  
   JPA implementation used to map Java entities to database tables.

6. PostgreSQL  
   Relational database used to store ticket data.

7. Maven  
   Build tool used for dependency management, tests, and packaging.

8. Jakarta Validation  
   Used for request validation rules.

9. Spring Boot Actuator  
   Used for technical health checks.

10. JUnit  
    Used for automated tests.

11. Mockito  
    Used for isolated service and controller tests with mocked dependencies.

12. AssertJ  
    Used for readable test assertions.

13. MockMvc  
    Used for testing Spring MVC controllers without starting a real server.

14. GitHub  
    Used for version control and project hosting.

## Architecture Overview

```text
Client
  ↓
TicketController
  ↓
TicketService
  ↓
TicketRepository
  ↓
PostgreSQL
```

## Data Model

The central entity of the application is `Ticket`.

A ticket contains the following fields:

1. `id`
2. `title`
3. `description`
4. `status`
5. `priority`
6. `assignedTo`
7. `createdAt`
8. `updatedAt`

### Ticket Status

A ticket can have one of the following status values:

1. `OPEN`
2. `IN_PROGRESS`
3. `RESOLVED`
4. `CLOSED`

### Ticket Priority

A ticket can have one of the following priority values:

1. `LOW`
2. `MEDIUM`
3. `HIGH`
4. `CRITICAL`

New tickets start with the default status `OPEN`.

## Entity Mapping

The `Ticket` class is mapped to the database table `tickets`.

Important mapping decisions:

1. `id` is the primary key.
2. `id` is generated automatically.
3. `title` is required.
4. `description` is required.
5. `status` is stored as a string enum.
6. `priority` is stored as a string enum.
7. `createdAt` is set when the ticket is created.
8. `updatedAt` is updated when the ticket changes.

## Repository Layer

The `TicketRepository` is the persistence layer for `Ticket` entities.

It extends:

```java
JpaRepository<Ticket, Long>
```

This provides standard persistence operations such as:

1. `save`
2. `findById`
3. `findAll`
4. `delete`
5. `deleteById`
6. `existsById`
7. `count`

Custom query methods:

1. `findByStatus`
2. `findByPriority`
3. `findByAssignedTo`
4. `findByStatusAndPriority`

These methods are derived query methods. Spring Data JPA creates the required queries based on the method names.

## API DTOs

The project separates the internal database entity from the external API contract.

Implemented DTOs:

1. `CreateTicketRequest`
2. `UpdateTicketRequest`
3. `UpdateTicketStatusRequest`
4. `TicketResponse`

### Validation Rules

1. `title` must not be blank.
2. `title` must be at most 120 characters.
3. `description` must not be blank.
4. `priority` must not be null.
5. `status` must not be null when updating ticket status.
6. `assignedTo` is optional, but must be at most 120 characters.

## Mapper Layer

The `TicketMapper` converts between `Ticket` entities and DTOs.

Implemented mapping methods:

1. `toEntity`
2. `toResponse`
3. `toResponseList`
4. `updateEntity`

The mapper keeps conversion logic out of the controller and service classes.

## Service Layer

The `TicketService` contains the business logic for ticket operations.

Implemented service methods:

1. `createTicket`
2. `getAllTickets`
3. `getTicketById`
4. `updateTicket`
5. `updateTicketStatus`
6. `deleteTicket`
7. `findByStatus`
8. `findByPriority`
9. `findByStatusAndPriority`

The service uses `TicketRepository` for persistence operations and `TicketMapper` for converting between entities and DTOs.

If a ticket does not exist, the service throws `TicketNotFoundException`.

Read operations use read only transactions. Write operations use regular transactions.

## REST API

The `TicketController` exposes ticket operations over HTTP.

Implemented endpoints:

```text
POST /api/tickets
GET /api/tickets
GET /api/tickets/{id}
PUT /api/tickets/{id}
PATCH /api/tickets/{id}/status
DELETE /api/tickets/{id}
GET /api/tickets?status=OPEN
GET /api/tickets?priority=HIGH
GET /api/tickets?status=OPEN&priority=HIGH
```

### Create Ticket

```http
POST /api/tickets
Content-Type: application/json
```

Request body:

```json
{
  "title": "Login not working",
  "description": "A user cannot log in to the internal dashboard.",
  "priority": "HIGH",
  "assignedTo": "IT Support"
}
```

Expected status:

```text
201 Created
```

Example response:

```json
{
  "id": 1,
  "title": "Login not working",
  "description": "A user cannot log in to the internal dashboard.",
  "status": "OPEN",
  "priority": "HIGH",
  "assignedTo": "IT Support",
  "createdAt": "2026-05-04T10:00:00",
  "updatedAt": "2026-05-04T10:00:00"
}
```

### Get All Tickets

```http
GET /api/tickets
```

Expected status:

```text
200 OK
```

### Get Ticket By ID

```http
GET /api/tickets/{id}
```

Expected status:

```text
200 OK
```

### Filter Tickets By Status

```http
GET /api/tickets?status=OPEN
```

Expected status:

```text
200 OK
```

### Filter Tickets By Priority

```http
GET /api/tickets?priority=HIGH
```

Expected status:

```text
200 OK
```

### Filter Tickets By Status And Priority

```http
GET /api/tickets?status=OPEN&priority=HIGH
```

Expected status:

```text
200 OK
```

### Update Ticket

```http
PUT /api/tickets/{id}
Content-Type: application/json
```

Request body:

```json
{
  "title": "Updated title",
  "description": "Updated description",
  "priority": "MEDIUM",
  "assignedTo": "Backend Team"
}
```

Expected status:

```text
200 OK
```

### Update Ticket Status

```http
PATCH /api/tickets/{id}/status
Content-Type: application/json
```

Request body:

```json
{
  "status": "IN_PROGRESS"
}
```

Expected status:

```text
200 OK
```

### Delete Ticket

```http
DELETE /api/tickets/{id}
```

Expected status:

```text
204 No Content
```

## Exception Handling

Currently implemented:

1. `TicketNotFoundException`

Not implemented yet:

1. Central exception handler
2. HTTP 404 mapping for missing tickets
3. HTTP 400 mapping for validation errors
4. Consistent API error response body
5. Error response format for invalid enum values

Centralized REST error handling will be implemented in a later step.

## Database

The application uses PostgreSQL as its relational database.

Local development database configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/incident_ticket_db
spring.datasource.username=incident_user
spring.datasource.password=incident_password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

The `tickets` table is generated from the `Ticket` JPA entity during local development.

## Start PostgreSQL Locally

For local development, PostgreSQL can be started with Docker:

```bash
docker run --name incident-ticket-postgres \
  -e POSTGRES_DB=incident_ticket_db \
  -e POSTGRES_USER=incident_user \
  -e POSTGRES_PASSWORD=incident_password \
  -p 5432:5432 \
  -d postgres:16
```

Check if the container is running:

```bash
docker ps
```

Start an existing PostgreSQL container:

```bash
docker start incident-ticket-postgres
```

Connect to the database:

```bash
docker exec -it incident-ticket-postgres psql -U incident_user -d incident_ticket_db
```

List tables:

```sql
\dt
```

Describe the `tickets` table:

```sql
\d tickets
```

## Local Start

Make sure PostgreSQL is running before starting the application.

Start the application:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

## Health Check

```text
http://localhost:8080/actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

## Test Endpoint

```text
http://localhost:8080/api/ping
```

Expected response:

```text
pong
```

## Manual API Testing

### Create a ticket

```bash
curl -i -X POST http://localhost:8080/api/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Login not working",
    "description": "A user cannot log in to the internal dashboard.",
    "priority": "HIGH",
    "assignedTo": "IT Support"
  }'
```

### Get all tickets

```bash
curl -i http://localhost:8080/api/tickets
```

### Get one ticket by ID

```bash
curl -i http://localhost:8080/api/tickets/1
```

### Filter tickets by status

```bash
curl -i "http://localhost:8080/api/tickets?status=OPEN"
```

### Filter tickets by priority

```bash
curl -i "http://localhost:8080/api/tickets?priority=HIGH"
```

### Update a ticket

```bash
curl -i -X PUT http://localhost:8080/api/tickets/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Updated title",
    "description": "Updated description",
    "priority": "MEDIUM",
    "assignedTo": "Backend Team"
  }'
```

### Update ticket status

```bash
curl -i -X PATCH http://localhost:8080/api/tickets/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "IN_PROGRESS"
  }'
```

### Delete a ticket

```bash
curl -i -X DELETE http://localhost:8080/api/tickets/1
```

## Tests

Run all tests:

```bash
./mvnw test
```

On Windows PowerShell:

```powershell
.\mvnw.cmd test
```

Currently implemented test categories:

1. Repository tests
2. DTO validation tests
3. Mapper tests
4. Service unit tests
5. Controller tests

Repository tests verify persistence operations and query methods.

DTO validation tests verify request validation rules.

Mapper tests verify conversion between entities and DTOs.

Service unit tests verify business logic with a mocked repository.

Controller tests verify request mappings, HTTP status codes, JSON responses, and request validation behavior with MockMvc.

## Project Structure

```text
src
  main
    java
      com
        example
          incidentticketapi
            controller
              PingController.java
              TicketController.java
            dto
              CreateTicketRequest.java
              TicketResponse.java
              UpdateTicketRequest.java
              UpdateTicketStatusRequest.java
            exception
              TicketNotFoundException.java
            mapper
              TicketMapper.java
            model
              Ticket.java
              TicketPriority.java
              TicketStatus.java
            repository
              TicketRepository.java
            service
              TicketService.java
            IncidentTicketApiApplication.java
    resources
      application.properties
  test
    java
      com
        example
          incidentticketapi
            controller
              TicketControllerTest.java
            dto
              CreateTicketRequestValidationTest.java
              UpdateTicketRequestValidationTest.java
              UpdateTicketStatusRequestValidationTest.java
            mapper
              TicketMapperTest.java
            repository
              TicketRepositoryTest.java
            service
              TicketServiceTest.java
            IncidentTicketApiApplicationTests.java
pom.xml
README.md
```

## Currently Implemented

1. Basic Spring Boot application setup
2. PostgreSQL connection
3. Ticket JPA entity
4. Ticket status enum
5. Ticket priority enum
6. Ticket repository
7. Repository query methods
8. Repository tests
9. Request and response DTOs
10. DTO validation rules
11. Ticket mapper
12. DTO validation tests
13. Mapper tests
14. Ticket service layer
15. Ticket business logic
16. TicketNotFoundException
17. Service unit tests
18. Ticket REST controller
19. Ticket CRUD endpoints
20. Ticket filter endpoints
21. Controller validation with `@Valid`
22. Controller tests with MockMvc
23. Health check endpoint
24. Ping endpoint

## Not Implemented Yet

1. Central validation error handling
2. Central exception handling
3. Consistent API error response body
4. OpenAPI documentation
5. Swagger UI
6. Dockerfile
7. Docker Compose setup
8. GitHub Actions CI pipeline
9. Integration tests with full API flow
10. Deployment configuration

## Roadmap

1. Implement central exception handling.
2. Add consistent API error responses.
3. Add OpenAPI documentation with Swagger UI.
4. Add Dockerfile.
5. Add Docker Compose setup for application and PostgreSQL.
6. Add GitHub Actions CI pipeline.
7. Add integration tests.
8. Add final architecture diagram and screenshots.