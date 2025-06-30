package com.example.learn.service;

import com.example.learn.model.Attendance;
import com.example.learn.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    public Attendance markAttendance(String meetingId, String studentId) {
        Attendance existing = attendanceRepository.findByMeetingIdAndStudentId(meetingId, studentId);
        if (existing == null) {
            Attendance attendance = new Attendance(meetingId, studentId);
            return attendanceRepository.save(attendance);
        }
        return existing;
    }

    public Attendance markLeave(String meetingId, String studentId) {
        Attendance attendance = attendanceRepository.findByMeetingIdAndStudentId(meetingId, studentId);
        if (attendance != null) {
            attendance.setLeaveTime(LocalDateTime.now());
            return attendanceRepository.save(attendance);
        }
        return null;
    }

    public List<Attendance> getMeetingAttendance(String meetingId) {
        return attendanceRepository.findByMeetingId(meetingId);
    }

    public List<Attendance> getStudentAttendance(String studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
}
