package com.example.learn.model;
//done
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = "meeting_analytics_snapshots")
public class MeetingAnalyticsSnapshot {
    @Id
    private String id;
    private String meetingId;
    private String timestamp;
    private String summary;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMeetingId() { return meetingId; }
    public void setMeetingId(String meetingId) { this.meetingId = meetingId; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
}