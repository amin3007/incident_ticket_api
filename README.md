# Incident Ticket API

A REST API for managing internal IT incident tickets.

## Project Goal

This project demonstrates backend development with Spring Boot, PostgreSQL, validation, testing, Docker, OpenAPI documentation, and a CI pipeline.

The application is designed as a portfolio project. It will provide a REST API for creating, reading, updating, deleting, and filtering internal IT incident tickets.

## Current Status

The basic Spring Boot setup is complete. The application is connected to a local PostgreSQL database. The initial ticket data model has been implemented as a JPA entity with status and priority enums. A Spring Data JPA repository has been added for basic persistence operations and query methods. Request and response DTOs have been added with validation rules for clean API input handling.

## Tech Stack

1. Java 21
2. Spring Boot
3. Spring Web
4. Spring Data JPA  
5. Hibernate
6. PostgreSQL
7. Maven
8. Spring Boot Actuator
9. GitHub
## Data Model

The central entity of the application is `Ticket`.

A ticket contains the following fields:

1. `id`
2. `title`
3. `description`
4. `status`
5. `priority`
6. `assignee`
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

## API DTOs

The project separates the internal database entity from the external API contract.

Implemented DTOs:

1. `CreateTicketRequest`
2. `UpdateTicketRequest`
3. `UpdateTicketStatusRequest`
4. `TicketResponse`

Validation rules:

1. `title` cannot be blank.
2. `title` cannot have more than 120 characters.
3. `description` cannot be blank.
4. `priority` cannot be null.
5. `status` cannot be null when updating ticket status.
6. `assignee` is optional, but cannot be more than 120 characters.

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

Connect to the database:

```bash
docker exec -it incident-ticket-postgres psql -U incident_user -d incident_ticket_db
```

List tables:

```sql
\dt
```

Describe the tickets table:

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

## Project Structure

```text
src
  main
    java
      com
        example
          incident_ticket_api
            controller
              PingController.java
            dto
              CreateTicketRequest.java
              TicketResponse.java
              UpdateTicketRequest.java
              UpdateTicketStatusRequest.java
            mapper
              TicketMapper.java
            model
              Ticket.java
              TicketPriority.java
              TicketStatus.java
            repository
              TicketRepository.java
            IncidentTicketApiApplication.java
    resources
      application.properties
  test
    java
      com
        example
          incidentticketapi
            dto
              CreateTicketRequestValidationTest.java
              UpdateTicketRequestValidationTest.java
              UpdateTicketStatusRequestValidationTest.java
            mapper
              TicketMapperTest.java
            repository
              TicketRepositoryTest.java
            IncidentTicketApiApplicationTests.java
pom.xml
README.md
```

## Known Limitations

The project is still in an early stage.

Currently implemented:

1. Basic Spring Boot application setup
2. PostgreSQL connection
3. Ticket JPA entity
4. Ticket status enum
5. Ticket priority enum
6. Health check endpoint
7. Ping endpoint
8. Ticket repository
9. Repository tests
10. Request and response DTOs
11. DTO validation rules
12. Ticket mapper
13. DTO validation tests
14. Mapper tests

Not implemented yet:

1. Ticket service layer
2. Ticket REST endpoints
3. Error handling
4. Automated service and controller tests
5. Docker Compose setup
6. OpenAPI documentation
7. GitHub Actions CI pipeline