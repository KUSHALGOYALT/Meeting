package com.example.learn.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "emotion_data")
public class EmotionData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String meetingId;

    @Column(nullable = false)
    private String emotion;

    @Column(nullable = false)
    private Double confidence;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    // Constructors
    public EmotionData(String string, String studentId, String engaged, LocalDateTime now) {}

    public EmotionData(String studentId, String meetingId, String emotion, Double confidence) {
        this.studentId = studentId;
        this.meetingId = meetingId;
        this.emotion = emotion;
        this.confidence = confidence;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getMeetingId() { return meetingId; }
    public void setMeetingId(String meetingId) { this.meetingId = meetingId; }

    public String getEmotion() { return emotion; }
    public void setEmotion(String emotion) { this.emotion = emotion; }

    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}