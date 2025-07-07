package com.example.learn.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
//done
@Document(collection = "attendance_records")
public class AttendanceRecord {
    @Id
    private String id;
    private String meetingId;
    private String participantEmail;
    private String participantName;
    private String joinTime;
    private String leaveTime;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMeetingId() { return meetingId; }
    public void setMeetingId(String meetingId) { this.meetingId = meetingId; }
    public String getParticipantEmail() { return participantEmail; }
    public void setParticipantEmail(String participantEmail) { this.participantEmail = participantEmail; }
    public String getParticipantName() { return participantName; }
    public void setParticipantName(String participantName) { this.participantName = participantName; }
    public String getJoinTime() { return joinTime; }
    public void setJoinTime(String joinTime) { this.joinTime = joinTime; }
    public String getLeaveTime() { return leaveTime; }
    public void setLeaveTime(String leaveTime) { this.leaveTime = leaveTime; }
}