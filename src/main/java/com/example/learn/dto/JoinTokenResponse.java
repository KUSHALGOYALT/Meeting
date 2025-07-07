package com.example.learn.dto;

public class JoinTokenResponse {
    private String token;
    private String meetingId;

    // Getters and setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getMeetingId() { return meetingId; }
    public void setMeetingId(String meetingId) { this.meetingId = meetingId; }
}