package com.example.learn.dto;
// done
public class FatigueDataRequest {
    private String participantId;
    private String meetingId;
    private String status;
    private double score;
    private String timestamp;

    // Getters and setters
    public String getParticipantId() { return participantId; }
    public void setParticipantId(String participantId) { this.participantId = participantId; }
    public String getMeetingId() { return meetingId; }
    public void setMeetingId(String meetingId) { this.meetingId = meetingId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}