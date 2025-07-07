package com.example.learn.service;

import com.example.learn.model.AttendanceRecord;
import com.example.learn.repository.AttendanceRecordRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;

    public List<AttendanceRecord> getMeetingAttendance(String meetingId) {
        return attendanceRecordRepository.findByMeetingId(meetingId);
    }

    public List<AttendanceRecord> getParticipantAttendance(String participantEmail) {
        return attendanceRecordRepository.findByParticipantEmail(participantEmail);
    }

    public byte[] exportAttendanceToExcel(String meetingId) {
        List<AttendanceRecord> records = attendanceRecordRepository.findByMeetingId(meetingId);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Attendance");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Participant Email");
            header.createCell(1).setCellValue("Participant Name");
            header.createCell(2).setCellValue("Join Time");
            header.createCell(3).setCellValue("Leave Time");

            int rowNum = 1;
            for (AttendanceRecord record : records) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(record.getParticipantEmail());
                row.createCell(1).setCellValue(record.getParticipantName());
                row.createCell(2).setCellValue(record.getJoinTime());
                row.createCell(3).setCellValue(record.getLeaveTime() != null ? record.getLeaveTime() : "N/A");
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Excel file", e);
        }
    }
}