package com.example.learn.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "learning_paths")
public class LearningPath {
    @Id
    private String id;
    private String sessionId;
    private String studentId;
    private String pathDescription;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getPathDescription() { return pathDescription; }
    public void setPathDescription(String pathDescription) { this.pathDescription = pathDescription; }
}