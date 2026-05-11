# Incident Ticket API

A backend portfolio project for managing internal IT incident tickets through a REST API.

The project demonstrates backend development with Spring Boot, PostgreSQL, layered architecture, validation, automated tests, centralized error handling, OpenAPI documentation, operational health checks, Docker, Docker Compose, GitHub Actions CI, and full API integration testing.

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
- Docker Compose
- GitHub Actions
- JUnit, Mockito, AssertJ, MockMvc

## Features

- REST API for incident ticket management
- PostgreSQL persistence with JPA entity mapping
- Layered architecture: Controller, Service, Repository, DTOs, Mapper
- Request validation with Jakarta Validation
- Centralized JSON error handling
- OpenAPI documentation with Swagger UI
- Actuator health, info, metrics, liveness and readiness endpoints
- Custom health indicator for the ticket repository
- Dockerfile for containerized application startup
- Docker Compose setup for API and PostgreSQL
- GitHub Actions CI pipeline for automated build, tests, Compose validation, and Docker image build
- Full API integration test covering the complete ticket lifecycle
- Automated tests for repository, DTO validation, mapper, service, controller, error handling, health indicator, and API integration

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

## Environment Configuration

The repository contains an `.env.example` file with example values.

Create a local `.env` file from it:

```bash
cp .env.example .env
```

On Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

Example environment values:

```properties
POSTGRES_DB=incident_ticket_db
POSTGRES_USER=incident_user
POSTGRES_PASSWORD=incident_password

SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/incident_ticket_db
SPRING_DATASOURCE_USERNAME=incident_user
SPRING_DATASOURCE_PASSWORD=incident_password
```

The local `.env` file should not be committed.

## Docker Compose Setup

Build and start the full local stack:

```bash
docker compose up --build
```

Run in detached mode:

```bash
docker compose up --build -d
```

Check running services:

```bash
docker compose ps
```

View logs:

```bash
docker compose logs api
docker compose logs db
```

Stop the stack:

```bash
docker compose down
```

Stop the stack and remove the database volume:

```bash
docker compose down -v
```

The Compose setup starts:

- Spring Boot API
- PostgreSQL database
- Persistent PostgreSQL volume
- Internal Docker network

The API connects to PostgreSQL through the Compose service name:

```text
db
```

## Docker Image

Build the API image manually:

```bash
docker build -t incident-ticket-api:latest .
```

Run the API container manually against a local PostgreSQL setup:

```bash
docker run -d --name incident-ticket-api \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/incident_ticket_db \
  -e SPRING_DATASOURCE_USERNAME=incident_user \
  -e SPRING_DATASOURCE_PASSWORD=incident_password \
  incident-ticket-api:latest
```

Docker Compose is the recommended local setup because it starts both the API and PostgreSQL together.

## Local Maven Start

Start PostgreSQL first:

```bash
docker compose up -d db
```

Then start the application:

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

## Tests

Repository and integration tests require a running PostgreSQL database.

Start PostgreSQL before running tests locally:

```bash
docker compose up -d db
```

Run all tests:

```bash
./mvnw test
```

On Windows PowerShell:

```powershell
docker compose up -d db
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
- Full API integration tests

The full API integration test verifies a complete ticket lifecycle:

1. Create ticket
2. Read ticket by ID
3. Update ticket
4. Update ticket status
5. Filter tickets
6. Delete ticket
7. Verify `404 Not Found` response after deletion

## Continuous Integration

The project uses GitHub Actions for continuous integration.

The CI pipeline runs on pushes and pull requests and performs:

- Java 21 setup
- Maven dependency caching
- PostgreSQL service container startup
- Maven build and tests
- Docker Compose configuration validation
- Docker image build

Workflow file:

```text
.github/workflows/ci.yml
```

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
            integration
            mapper
            repository
            service
.github
  workflows
    ci.yml
compose.yaml
.env.example
Dockerfile
.dockerignore
pom.xml
README.md
```

## Roadmap

Planned next steps:

- Add final architecture diagram
- Add screenshots of Swagger UI, GitHub Actions, and Docker Compose
- Prepare final CV project description