package com.example.learn.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "face_data")
public class FaceData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String meetingId;

    @Column(nullable = false, length = 10000)
    private String faceEncoding;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();
    @Column(nullable = false)
    private String verified;

    // Constructors
    public FaceData() {}

    public FaceData(String studentId, String meetingId, String faceEncoding) {
        this.studentId = studentId;
        this.meetingId = meetingId;
        this.faceEncoding = faceEncoding;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getMeetingId() { return meetingId; }
    public void setMeetingId(String meetingId) { this.meetingId = meetingId; }

    public String getFaceEncoding() { return faceEncoding; }
    public void setFaceEncoding(String faceEncoding) { this.faceEncoding = faceEncoding; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public void setVerified(boolean verified) {
        this.verified = String.valueOf(verified);
    }

    public String isVerified() {
        return verified;
    }
}
