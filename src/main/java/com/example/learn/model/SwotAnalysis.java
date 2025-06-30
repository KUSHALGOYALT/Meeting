package com.example.learn.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

// SWOT Analysis Entity
@Document(collection = "swot_analysis")
public class SwotAnalysis {
    @Id
    private String id;
    private String studentId;
    private String subject;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> opportunities;
    private List<String> threats;
    private LocalDateTime createdAt;
    private String aiRecommendations;

    // Constructors
    public SwotAnalysis() {}

    public SwotAnalysis(String studentId, String subject) {
        this.studentId = studentId;
        this.subject = subject;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public List<String> getStrengths() { return strengths; }
    public void setStrengths(List<String> strengths) { this.strengths = strengths; }

    public List<String> getWeaknesses() { return weaknesses; }
    public void setWeaknesses(List<String> weaknesses) { this.weaknesses = weaknesses; }

    public List<String> getOpportunities() { return opportunities; }
    public void setOpportunities(List<String> opportunities) { this.opportunities = opportunities; }

    public List<String> getThreats() { return threats; }
    public void setThreats(List<String> threats) { this.threats = threats; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getAiRecommendations() { return aiRecommendations; }
    public void setAiRecommendations(String aiRecommendations) { this.aiRecommendations = aiRecommendations; }
}
