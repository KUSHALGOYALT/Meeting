package com.example.learn.model;

import java.util.List;

public class Quiz {
    private String id;
    private String title;
    private String subject;
    private List<Question> questions;
    private String teacherId;
    private String duration;
    private String difficulty;
    private String dueDate;
    private String status; // "available" or "locked"

    public Quiz() {}

    public Quiz(String id, String title, String subject, List<Question> questions, String teacherId, String duration, String difficulty, String dueDate, String status) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.questions = questions;
        this.teacherId = teacherId;
        this.duration = duration;
        this.difficulty = difficulty;
        this.dueDate = dueDate;
        this.status = status;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}