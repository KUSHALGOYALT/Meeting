package com.example.learn.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "weekly_quizzes")
public class WeeklyQuiz {
    @Id
    private String id;
    private String sessionId;
    private String content;
    private Map<String, Map<String, Boolean>> studentAnswers; // studentId -> question -> answer

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Map<String, Map<String, Boolean>> getStudentAnswers() { return studentAnswers; }
    public void setStudentAnswers(Map<String, Map<String, Boolean>> studentAnswers) { this.studentAnswers = studentAnswers; }
}