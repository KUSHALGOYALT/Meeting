package com.example.learn.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

// Face Recognition Data Entity
@Document(collection = "face_data")
public class FaceData {
    @Id
    private String id;
    private String studentId;
    private String meetingId;
    private String faceEncoding; // Base64 encoded face features
    private boolean verified;
    private LocalDateTime timestamp;

    // Constructors
    public FaceData() {}

    public FaceData(String studentId, String meetingId, String faceEncoding) {
        this.studentId = studentId;
        this.meetingId = meetingId;
        this.faceEncoding = faceEncoding;
        this.verified = false;
        this.timestamp = LocalDateTime.now();
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

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
