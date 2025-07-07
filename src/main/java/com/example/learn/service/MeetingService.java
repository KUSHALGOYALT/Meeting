package com.example.learn.service;

import com.example.learn.dto.CreateMeetingRequest;
import com.example.learn.dto.CreateTranscriptRequest;
import com.example.learn.dto.JoinTokenResponse;
import com.example.learn.model.AttendanceRecord;
import com.example.learn.model.Meeting;
import com.example.learn.model.TranscriptLine;
import com.example.learn.repository.AttendanceRecordRepository;
import com.example.learn.repository.MeetingRepository;
import com.example.learn.repository.TranscriptLineRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

@Service
public class MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;

    @Autowired
    private TranscriptLineRepository transcriptLineRepository;

    public Meeting createMeeting(CreateMeetingRequest request, String teacherEmail, String teacherName) {
        Meeting meeting = new Meeting();
        meeting.setId(UUID.randomUUID().toString());
        meeting.setTitle(request.getTitle());
        meeting.setTeacherEmail(teacherEmail);
        meeting.setTeacherName(teacherName);
        meeting.setStartTime(request.getStartTime());
        meeting.setActive(true);
        return meetingRepository.save(meeting);
    }

    public JoinTokenResponse joinMeeting(String meetingId, String userEmail, String userName) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found"));
        if (!meeting.isActive()) {
            throw new IllegalStateException("Meeting is not active");
        }

        AttendanceRecord record = new AttendanceRecord();
        record.setId(UUID.randomUUID().toString());
        record.setMeetingId(meetingId);
        record.setParticipantEmail(userEmail);
        record.setParticipantName(userName);
        record.setJoinTime(new java.util.Date().toString());
        attendanceRecordRepository.save(record);

        JoinTokenResponse response = new JoinTokenResponse();
        response.setMeetingId(meetingId);
        response.setToken(UUID.randomUUID().toString()); // Replace with actual token logic
        return response;
    }

    public void leaveMeeting(String participantId) {
        AttendanceRecord record = attendanceRecordRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found"));
        record.setLeaveTime(new java.util.Date().toString());
        attendanceRecordRepository.save(record);
    }

    public void endMeeting(String meetingId, String teacherEmail) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found"));
        if (!meeting.getTeacherEmail().equals(teacherEmail)) {
            throw new IllegalStateException("Only the teacher can end the meeting");
        }
        meeting.setActive(false);
        meeting.setEndTime(new java.util.Date().toString());
        meetingRepository.save(meeting);
    }

    public void addTranscriptLine(CreateTranscriptRequest request) { // Updated to CreateTranscriptRequest
        TranscriptLine line = new TranscriptLine();
        line.setId(UUID.randomUUID().toString());
        line.setMeetingId(request.getMeetingId());
        line.setParticipantId(request.getParticipantId());
        line.setText(request.getText());
        line.setTimestamp(request.getTimestamp());
        transcriptLineRepository.save(line);
    }

    public List<Meeting> getTeacherMeetings(String teacherEmail) {
        return meetingRepository.findByTeacherEmail(teacherEmail);
    }

    public List<Meeting> getActiveMeetings() {
        return meetingRepository.findByActiveTrue();
    }

    public List<TranscriptLine> getMeetingTranscript(String meetingId) {
        return transcriptLineRepository.findByMeetingId(meetingId);
    }

    public List<AttendanceRecord> getMeetingAttendance(String meetingId) {
        return attendanceRecordRepository.findByMeetingId(meetingId);
    }

    public byte[] exportNotesToExcel(String meetingId) {
        List<TranscriptLine> notes = transcriptLineRepository.findByMeetingId(meetingId);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Notes");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Participant ID");
            header.createCell(1).setCellValue("Text");
            header.createCell(2).setCellValue("Timestamp");

            int rowNum = 1;
            for (TranscriptLine note : notes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(note.getParticipantId());
                row.createCell(1).setCellValue(note.getText());
                row.createCell(2).setCellValue(note.getTimestamp());
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate notes Excel file", e);
        }
    }
}