package com.example.learn.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "engagements")
public class Engagement {
    private String id;
    private String sessionId;
    private String studentId;
    private double confusion;
    private double distraction;
    private double engagement;
    private double frustrationScore;
    private double confidenceScore;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public double getConfusion() { return confusion; }
    public void setConfusion(double confusion) { this.confusion = confusion; }
    public double getDistraction() { return distraction; }
    public void setDistraction(double distraction) { this.distraction = distraction; }
    public double getEngagement() { return engagement; }
    public void setEngagement(double engagement) { this.engagement = engagement; }
    public double getFrustrationScore() { return frustrationScore; }
    public void setFrustrationScore(double score) { this.frustrationScore = score; }
    public double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(double score) { this.confidenceScore = score; }
}