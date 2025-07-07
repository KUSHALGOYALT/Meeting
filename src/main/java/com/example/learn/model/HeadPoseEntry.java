package com.example.learn.model;
//done
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = "head_pose_entries")
public class HeadPoseEntry {
    @Id
    private String id;
    private String participantId;
    private String meetingId;
    private double yaw;
    private double pitch;
    private double roll;
    private String timestamp;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getParticipantId() { return participantId; }
    public void setParticipantId(String participantId) { this.participantId = participantId; }
    public String getMeetingId() { return meetingId; }
    public void setMeetingId(String meetingId) { this.meetingId = meetingId; }
    public double getYaw() { return yaw; }
    public void setYaw(double yaw) { this.yaw = yaw; }
    public double getPitch() { return pitch; }
    public void setPitch(double pitch) { this.pitch = pitch; }
    public double getRoll() { return roll; }
    public void setRoll(double roll) { this.roll = roll; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}