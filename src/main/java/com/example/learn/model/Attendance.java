package com.example.learn.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

// Attendance Entity
@Document(collection = "attendance")
public class Attendance {
    @Id
    private String id;
    private String meetingId;
    private String studentId;
    private LocalDateTime joinTime;
    private LocalDateTime leaveTime;
    private boolean present;

    // Constructors
    public Attendance() {}

    public Attendance(String meetingId, String studentId) {
        this.meetingId = meetingId;
        this.studentId = studentId;
        this.joinTime = LocalDateTime.now();
        this.present = true;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMeetingId() { return meetingId; }
    public void setMeetingId(String meetingId) { this.meetingId = meetingId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public LocalDateTime getJoinTime() { return joinTime; }
    public void setJoinTime(LocalDateTime joinTime) { this.joinTime = joinTime; }

    public LocalDateTime getLeaveTime() { return leaveTime; }
    public void setLeaveTime(LocalDateTime leaveTime) { this.leaveTime = leaveTime; }

    public boolean isPresent() { return present; }
    public void setPresent(boolean present) { this.present = present; }
}
