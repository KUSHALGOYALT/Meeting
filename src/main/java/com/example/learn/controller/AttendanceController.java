package com.example.learn.controller;

import com.example.learn.model.AttendanceRecord;
import com.example.learn.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:8080")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/meeting/{meetingId}")
    public ResponseEntity<?> getMeetingAttendance(@PathVariable String meetingId) {
        try {
            List<AttendanceRecord> attendance = attendanceService.getMeetingAttendance(meetingId);
            return ResponseEntity.ok(attendance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to retrieve meeting attendance: " + e.getMessage());
        }
    }

    @GetMapping("/participant/{participantEmail}")
    public ResponseEntity<?> getParticipantAttendance(@PathVariable String participantEmail) {
        try {
            List<AttendanceRecord> attendance = attendanceService.getParticipantAttendance(participantEmail);
            return ResponseEntity.ok(attendance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to retrieve participant attendance: " + e.getMessage());
        }
    }

    @GetMapping("/export/{meetingId}")
    public ResponseEntity<?> exportAttendanceToExcel(@PathVariable String meetingId) {
        try {
            byte[] excelFile = attendanceService.exportAttendanceToExcel(meetingId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "attendance_report_" + meetingId + ".xlsx");
            headers.setContentLength(excelFile.length);

            return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to export attendance: " + e.getMessage());
        }
    }
}