package com.example.learn.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sessions")
public class Session {
    @Id
    private String id;
    private String studentId;
    private String subject;
    private LocalDateTime createdAt;
    private String teacherId; // Added teacherId field

    // Constructor used in SessionService
    public Session(String id, String studentId, String subject, LocalDateTime createdAt, String teacherId) {
        this.id = id;
        this.studentId = studentId;
        this.subject = subject;
        this.createdAt = createdAt;
        this.teacherId = teacherId;
    }

    // Default constructor for MongoDB
    public Session() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
}