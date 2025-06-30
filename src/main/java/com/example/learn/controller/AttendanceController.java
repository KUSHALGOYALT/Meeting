package com.example.learn.controller;

import com.example.learn.model.Attendance;
import com.example.learn.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:3001")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/mark")
    public Attendance markAttendance(@RequestParam String meetingId, @RequestParam String studentId) {
        return attendanceService.markAttendance(meetingId, studentId);
    }

    @PutMapping("/leave")
    public ResponseEntity<Attendance> markLeave(@RequestParam String meetingId, @RequestParam String studentId) {
        Attendance attendance = attendanceService.markLeave(meetingId, studentId);
        return attendance != null ? ResponseEntity.ok(attendance) : ResponseEntity.notFound().build();
    }

    @GetMapping("/meeting/{meetingId}")
    public List<Attendance> getMeetingAttendance(@PathVariable String meetingId) {
        return attendanceService.getMeetingAttendance(meetingId);
    }

    @GetMapping("/student/{studentId}")
    public List<Attendance> getStudentAttendance(@PathVariable String studentId) {
        return attendanceService.getStudentAttendance(studentId);
    }
}
