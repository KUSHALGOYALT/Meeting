package com.example.learn.dto;

public class CreateMeetingRequest {
    private String title;
    private String startTime;
    private String duration;

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
}