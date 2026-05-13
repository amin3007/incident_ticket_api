# Architecture

This document describes the high-level architecture of the Incident Ticket API.

## Overview

The application follows a layered backend architecture. Each layer has a clearly separated responsibility.

```text
Client / Swagger UI / API Consumer
        |
        v
TicketController
        |
        v
TicketService
        |
        v
TicketRepository
        |
        v
PostgreSQL
```

## Layer Responsibilities

### Controller Layer

The controller layer exposes the REST API.

Main class:

```text
TicketController
```

Responsibilities:

```text
Receive HTTP requests
Validate request DTOs
Read path variables and query parameters
Call the service layer
Return HTTP responses
```

The controller does not contain business logic.

## Service Layer

The service layer contains the business logic for ticket operations.

Main class:

```text
TicketService
```

Responsibilities:

```text
Create tickets
Read tickets
Update tickets
Update ticket status
Delete tickets
Filter tickets by status and priority
Throw TicketNotFoundException when a ticket does not exist
```

## Repository Layer

The repository layer handles database access.

Main interface:

```text
TicketRepository
```

Responsibilities:

```text
Persist Ticket entities
Read Ticket entities from PostgreSQL
Provide query methods for status and priority filters
```

## Database Layer

The database layer stores ticket data in PostgreSQL.

Main table:

```text
tickets
```

Main fields:

```text
id
title
description
status
priority
assigned_to
created_at
updated_at
```

## Request Flow

```text
HTTP Request
  |
  v
TicketController
  |
  v
DTO Validation
  |
  v
TicketService
  |
  v
TicketRepository
  |
  v
PostgreSQL
  |
  v
Ticket Entity
  |
  v
TicketResponse DTO
  |
  v
HTTP Response
```

## Docker Compose Runtime

The project can be started as a local multi-container setup with Docker Compose.

```text
Host Machine
    |
    | docker compose up --build
    v

Docker Compose Network
    |
    |-- api service
    |     Spring Boot application
    |     Exposed port: 8080
    |     Connects to database through host name: db
    |
    |-- db service
          PostgreSQL database
          Exposed port: 5432
          Persistent volume: postgres_data
```

## Runtime Services

```text
api
```

Runs the Spring Boot application from the Dockerfile.

```text
db
```

Runs PostgreSQL 16 and stores ticket data in a persistent Docker volume.

## Database Connection in Docker Compose

Inside the Docker Compose network, the API connects to PostgreSQL through the service name:

```text
db
```

Datasource URL:

```text
jdbc:postgresql://db:5432/incident_ticket_db
```

## Error Handling Flow

```text
Exception
  |
  v
GlobalExceptionHandler
  |
  v
ApiErrorResponse
  |
  v
JSON error response
```

Handled examples:

```text
TicketNotFoundException -> 404 Not Found
Validation errors       -> 400 Bad Request
Malformed JSON          -> 400 Bad Request
Invalid enum values     -> 400 Bad Request
Unexpected errors       -> 500 Internal Server Error
```

## Operational Endpoints

The application exposes operational checks through Spring Boot Actuator.

```text
/actuator/health
/actuator/info
/actuator/metrics
/actuator/health/liveness
/actuator/health/readiness
/livez
/readyz
```

The health endpoint includes:

```text
Application health
Database health
Disk space health
Custom ticket repository health check
```

## Testing Architecture

The project contains multiple test layers.

```text
Unit and layer tests
  |
  |-- DTO validation tests
  |-- Mapper tests
  |-- Service tests with mocked repository
  |-- Controller tests with mocked service
  |-- Health indicator tests

Integration tests
  |
  |-- Full API flow through Controller, Service, Repository and PostgreSQL
```

The full API integration test verifies:

```text
Create ticket
Read ticket by ID
Update ticket
Update ticket status
Filter tickets
Delete ticket
Verify 404 Not Found after deletion
```

## CI Pipeline

GitHub Actions validates the project automatically.

```text
Push or Pull Request
        |
        v
GitHub Actions CI
        |
        |-- Set up Java 21
        |-- Start PostgreSQL service container
        |-- Run Maven build and tests
        |-- Validate Docker Compose configuration
        |-- Build Docker image
```

## Summary

The Incident Ticket API is structured as a maintainable Spring Boot backend with a clear separation between API, business logic and persistence. It includes PostgreSQL persistence, DTO validation, centralized error handling, OpenAPI documentation, Actuator health checks, Docker Compose runtime support, GitHub Actions CI and full API integration testing.