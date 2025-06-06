package com.example.learn.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "challenges")
public class Challenge {
    private String id;
    private String sessionId;
    private String title;
    private String description;
    private int points;
    private Map<String, Integer> studentPoints; // studentId -> points earned

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
    public Map<String, Integer> getStudentPoints() { return studentPoints; }
    public void setStudentPoints(Map<String, Integer> studentPoints) { this.studentPoints = studentPoints; }

    public void setQuestions(List<String> questions) {
    }
}