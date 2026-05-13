# Architecture

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
# Runtime with Docker Compose

```text
Host Machine
    |
    | docker compose up --build
    v

Docker Network
    |
    |-- api service
    |     Spring Boot application
    |     Port: 8080
    |
    |-- db service
          PostgreSQL
          Port: 5432
          Volume: postgres_data

```

# HTTP Request

```text
HTTP Request
  -> Controller
  -> DTO validation
  -> Service business logic
  -> Repository persistence
  -> PostgreSQL
  -> Response DTO
  -> HTTP Response

```