package com.example.learn.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

// Meeting/Class Entity
@Document(collection = "meetings")
public class Meeting {
    @Id
    private String id;
    private String title;
    private String teacherId;
    private String roomName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status; // SCHEDULED, ACTIVE, COMPLETED

    // Constructors
    public Meeting() {}

    public Meeting(String title, String teacherId, String roomName) {
        this.title = title;
        this.teacherId = teacherId;
        this.roomName = roomName;
        this.status = "SCHEDULED";
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
