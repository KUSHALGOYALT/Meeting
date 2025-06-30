package com.example.learn.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "emotion_data")
public class EmotionData {
    @Id
    private String id;
    private String studentId;
    private String meetingId;
    private String emotion;
    private Double confidence;
    private LocalDateTime timestamp = LocalDateTime.now();

    public EmotionData(String id, String studentId, String emotion, LocalDateTime timestamp) {}

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