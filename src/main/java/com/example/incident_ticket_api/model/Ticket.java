package com.example.incident_ticket_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity //indicates that this class is a JPA entity, which means it will be mapped to a database table
@Table(name = "tickets") //specifies the name of the database table that this entity will be mapped to
public class Ticket{
    @Id //indicates that this field is the primary key of the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY) //specifies that the value of this field will be generated automatically by the database, using an auto-incrementing strategy
    private Long id;

    @Column(nullable = false, length = 120) //specifies that this column cannot be null and has a maximum length of 120 characters
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TicketStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TicketPriority priority;

    @Column(length = 120)
    private String assignedTo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /** The default constructor is required by JPA to create instances of the entity.
     *  It is protected to prevent direct instantiation of the Ticket class without using the constructor that takes parameters.
     */
    protected Ticket(){
    }

    /** This constructor allows you to create a new Ticket instance with the specified title, description, status, priority, and assignedTo values.
     *  The createdAt and updatedAt fields will be automatically set when the entity is persisted to the database.
     */
    public Ticket(String title, String description, TicketStatus status, TicketPriority priority, String assignedTo) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assignedTo = assignedTo;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if(this.status == null) {
            this.status = TicketStatus.OPEN;
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
