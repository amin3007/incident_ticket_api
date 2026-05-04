# Incident Ticket Api: Project Scope

Goal of this document is to define the project scope for the Incident Ticket API project. This document will outline the features and functionalities that will be included in the project, as well as any limitations or exclusions.

## tecnical scenario
The Incident Ticket API will manage internal incident tickets for a company or technical team. The API will allow users to create, update, and retrieve incident tickets, as well as manage the status and priority of each ticket.

## project goal 
The goal of this project is to provide a rest API that allows users to easily manage their incident tickets, improving the efficiency and organization of the incident management process.

## features and functionalities
Version 1.0 of the Incident Ticket API will include the following features and functionalities:
1. Create Incident Ticket 
2. Show all Incident Tickets
3. Update Incident Ticket
4. Delete Incident Ticket
5. Sort Incident Tickets by priority and status
6. Search Incident Tickets by ID
7. API Documentation
8. Automated Testing
9. Docker Setup
10. CI Pipeline

## limitations and exclusions
The following features and functionalities will not be included in Version 1.0 of the Incident Ticket API:
1. User authentication and authorization
2. Role-based access control
3. Front-end UI
4. Integration with third-party services (e.g., email notifications, chat integrations)
5. File uploads and attachments
6. Full-text search capabilities
7. Kubernetes deployment
8. Cloud deployment

## central entity
The central entity in the Incident Ticket API is the "Incident Ticket". Each ticket will have the following attributes:
1. ID
2. Title
3. Description
4. Status
5. Priority
6. Created At
7. Updated At
8. Assigned To

## allowed status values
The allowed status values for an incident ticket will be:
1. OPEN
2. IN_PROGRESS
3. RESOLVED
4. CLOSED

## allowed priority values
The allowed priority values for an incident ticket will be:
1. LOW
2. MEDIUM
3. HIGH
4. CRITICAL

## planned api features
1. Create Incident Ticket: This endpoint will allow users to create a new incident ticket by providing the necessary information such as title, description, status, priority, and assigned user.
2. Show all Incident Tickets: This endpoint will allow users to retrieve a list of all incident tickets.
3. Update ticket: This endpoint will allow users to update the details of an existing incident ticket, such as changing the status, priority, or assigned user.
4. Delete ticket: This endpoint will allow users to delete an existing incident ticket.
5. Chande ticket status: This endpoint will allow users to change the status of an incident ticket.
6. Sort ticket by status and priority: This endpoint will allow users to sort incident tickets based on their status and priority.

## planned REST endpoints
POST /api/tickets - Create a new incident ticket
GET /api/tickets - Retrieve a list of all incident tickets
GET /api/tickets/{id} - Retrieve details of a specific incident ticket by ID
PUT /api/tickets/{id} - Update an existing incident ticket by ID
PATCH /api/tickets/{id}/status - Change the status of an incident ticket by ID
DELETE /api/tickets/{id} - Delete an existing incident ticket by ID
GET /api/tickets/?status=OPEN - Retrieve a list of incident tickets filtered by status
GET /api/tickets/?priority=HIGH - Retrieve a list of incident tickets filtered by priority

## short README
A containerized REST API for managing internal incident tickets, built with Spring Boot and Java. The API allows users to create, update, retrieve, and delete incident tickets, as well as manage their status and priority. The project includes automated testing, API documentation, and a CI pipeline for continuous integration and deployment.

## Explanation in 5 to 8 Sentences

I am building a REST API for managing internal IT incident tickets. A ticket represents a technical issue, system problem, or support request. The central entity of the project is the ticket, which contains the fields id, title, description, status, priority, assignee, createdAt, and updatedAt. The API allows clients to create, read, update, delete, and filter tickets by status or priority. Each ticket has a fixed status such as OPEN, IN_PROGRESS, RESOLVED, or CLOSED. Each ticket also has a priority such as LOW, MEDIUM, HIGH, or CRITICAL. Version 1 intentionally does not include user login, a frontend, or complex role management, so the backend project can be completed within a realistic time frame. The project is intended to demonstrate structured backend development with API design, database persistence, automated tests, Docker, documentation, and a CI pipeline.

## Selfcheck

1. What is the main goal of my project?

2. What is the central entity of the application?

3. Which fields does a ticket contain?

4. Which status values can a ticket have?

5. Which priority values can a ticket have?

6. Which features are included in Version 1?

7. Which features are intentionally not included in Version 1?

8. Which REST endpoints am I planning to implement?

9. Why is the project scope realistic for a time frame of 1 to 3 weeks?

10. How can I explain the project in 5 to 8 sentences?

