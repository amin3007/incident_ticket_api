# Incident Ticket API

A backend portfolio project for managing internal IT incident tickets through a REST API.

The project demonstrates practical backend development with Spring Boot, PostgreSQL, layered architecture, validation, automated tests, centralized error handling, OpenAPI documentation, operational health checks, and Docker containerization.

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- PostgreSQL
- Maven
- Jakarta Validation
- Spring Boot Actuator
- springdoc-openapi
- Docker
- JUnit, Mockito, AssertJ, MockMvc

## Current Features

- REST API for incident ticket management
- PostgreSQL persistence with JPA entity mapping
- Layered architecture: Controller, Service, Repository, DTOs, Mapper
- Request validation with Jakarta Validation
- Centralized JSON error handling
- OpenAPI documentation with Swagger UI
- Actuator health, info, metrics, liveness and readiness endpoints
- Custom health indicator for the ticket repository
- Dockerfile for containerized application startup
- Automated tests for repository, DTO validation, mapper, service, controller and health indicator

## Architecture

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

Main package:

```text
com.example.incident_ticket_api
```

## Data Model

The central entity is `Ticket`.

Fields:

```text
id
title
description
status
priority
assignedTo
createdAt
updatedAt
```

Ticket status values:

```text
OPEN
IN_PROGRESS
RESOLVED
CLOSED
```

Ticket priority values:

```text
LOW
MEDIUM
HIGH
CRITICAL
```

New tickets start with status `OPEN`.

## REST API

Implemented endpoints:

```text
POST   /api/tickets
GET    /api/tickets
GET    /api/tickets/{id}
PUT    /api/tickets/{id}
PATCH  /api/tickets/{id}/status
DELETE /api/tickets/{id}

GET    /api/tickets?status=OPEN
GET    /api/tickets?priority=HIGH
GET    /api/tickets?status=OPEN&priority=HIGH
```

## Example Request

```http
POST /api/tickets
Content-Type: application/json
```

```json
{
  "title": "Login not working",
  "description": "A user cannot log in to the internal dashboard.",
  "priority": "HIGH",
  "assignedTo": "IT Support"
}
```

## Example Response

```json
{
  "id": 1,
  "title": "Login not working",
  "description": "A user cannot log in to the internal dashboard.",
  "status": "OPEN",
  "priority": "HIGH",
  "assignedTo": "IT Support",
  "createdAt": "2026-05-09T10:00:00",
  "updatedAt": "2026-05-09T10:00:00"
}
```

## Error Handling

The API uses centralized error handling with `GlobalExceptionHandler`.

Handled error cases:

- Missing ticket returns `404 Not Found`
- Validation errors return `400 Bad Request`
- Malformed JSON returns `400 Bad Request`
- Invalid enum values return `400 Bad Request`
- Invalid query parameters return `400 Bad Request`
- Unexpected internal errors return `500 Internal Server Error`

Example error response:

```json
{
  "timestamp": "2026-05-09T12:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/tickets",
  "fieldErrors": [
    {
      "field": "title",
      "message": "Title must not be blank"
    }
  ]
}
```

## OpenAPI Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

OpenAPI YAML:

```text
http://localhost:8080/v3/api-docs.yaml
```

The documentation includes:

- Ticket CRUD endpoints
- Filter endpoints
- Request and response schemas
- Error response schema
- HTTP status codes
- Path and query parameters

## Operational Readiness

The application uses Spring Boot Actuator for operational checks.

Available endpoints:

```text
http://localhost:8080/actuator/health
http://localhost:8080/actuator/info
http://localhost:8080/actuator/metrics
http://localhost:8080/actuator/health/liveness
http://localhost:8080/actuator/health/readiness
http://localhost:8080/livez
http://localhost:8080/readyz
```

The health endpoint includes:

- Application health
- Database health
- Disk space health
- Custom ticket repository health check

## Local Setup

Start PostgreSQL with Docker:

```bash
docker run --name incident-ticket-postgres \
  -e POSTGRES_DB=incident_ticket_db \
  -e POSTGRES_USER=incident_user \
  -e POSTGRES_PASSWORD=incident_password \
  -p 5432:5432 \
  -d postgres:16
```

Start an existing PostgreSQL container:

```bash
docker start incident-ticket-postgres
```

Start the application locally:

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

## Docker

Build the Docker image:

```bash
docker build -t incident-ticket-api:latest .
```

Run the API container:

```bash
docker run -d --name incident-ticket-api \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/incident_ticket_db \
  -e SPRING_DATASOURCE_USERNAME=incident_user \
  -e SPRING_DATASOURCE_PASSWORD=incident_password \
  incident-ticket-api:latest
```

On Windows PowerShell:

```powershell
docker run -d --name incident-ticket-api `
  -p 8080:8080 `
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/incident_ticket_db `
  -e SPRING_DATASOURCE_USERNAME=incident_user `
  -e SPRING_DATASOURCE_PASSWORD=incident_password `
  incident-ticket-api:latest
```

Check if the container is running:

```bash
docker ps
```

Check container logs:

```bash
docker logs incident-ticket-api
```

Check the health endpoint:

```text
http://localhost:8080/actuator/health
```

Stop and remove the API container:

```bash
docker rm -f incident-ticket-api
```

Note: This Dockerfile step runs the API container against the local PostgreSQL setup. A full multi-container setup with API and PostgreSQL will be added with Docker Compose in the next step.

## Configuration

Local development configuration:

```properties
spring.application.name=incident_ticket_api
server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/incident_ticket_db
spring.datasource.username=incident_user
spring.datasource.password=incident_password
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs

management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
management.endpoint.health.show-components=always
management.endpoint.health.probes.enabled=true
management.endpoint.health.probes.add-additional-paths=true

management.info.env.enabled=true
info.app.name=Incident Ticket API
info.app.description=REST API for managing internal IT incident tickets
info.app.version=1.0.0
info.app.java.version=21
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

Implemented test areas:

- Repository tests
- DTO validation tests
- Mapper tests
- Service unit tests
- Controller tests
- Error handling tests
- Health indicator tests

## Project Structure

```text
src
  main
    java
      com
        example
          incident_ticket_api
            config
            controller
            dto
            exception
            health
            mapper
            model
            repository
            service
    resources
      application.properties
  test
    java
      com
        example
          incident_ticket_api
            controller
            dto
            health
            mapper
            repository
            service
Dockerfile
.dockerignore
pom.xml
README.md
```

## Roadmap

Planned next steps:

- Add Docker Compose setup for application and PostgreSQL
- Add GitHub Actions CI pipeline
- Add full integration tests
- Add final architecture diagram and screenshots
- Prepare final CV project description