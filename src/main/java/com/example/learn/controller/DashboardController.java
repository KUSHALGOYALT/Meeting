package com.example.learn.controller;

import com.example.learn.service.AttendanceService;
import com.example.learn.service.EmotionService;
import com.example.learn.service.MeetingService;
import com.example.learn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:3002")
public class DashboardController {
    @Autowired
    private UserService userService;
    @Autowired
    private MeetingService meetingService;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private EmotionService emotionService;

    @GetMapping("/stats")
    public DashboardStats getDashboardStats() {
        DashboardStats stats = new DashboardStats();
        stats.totalStudents = userService.getStudents().size();
        stats.totalTeachers = userService.getTeachers().size();
        stats.totalMeetings = meetingService.getAllMeetings().size();
        return stats;
    }

    public static class DashboardStats {
        public int totalStudents;
        public int totalTeachers;
        public int totalMeetings;
    }
}
