package com.example.learn.controller;

import com.example.learn.model.Attendance;
import com.example.learn.service.MeetingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/meeting")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping("/{sessionId}/join")
    public ResponseEntity<String> joinMeeting(@PathVariable String sessionId,
                                              @RequestParam String userId,
                                              @RequestParam String userName) {
        String roomName = meetingService.createMeeting(sessionId, userId, userName);
        return ResponseEntity.ok(roomName);
    }

    @PostMapping("/{sessionId}/leave")
    public ResponseEntity<Void> leaveMeeting(@PathVariable String sessionId,
                                             @RequestParam String userId) {
        meetingService.recordParticipantLeft(sessionId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{sessionId}/emotion")
    public ResponseEntity<Void> updateEmotion(@PathVariable String sessionId,
                                              @RequestParam String userId) {
        meetingService.updateEmotion(sessionId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{sessionId}/student/{userId}")
    public ResponseEntity<Attendance> getStudentAttendance(@PathVariable String sessionId,
                                                           @PathVariable String userId) {
        return meetingService.getStudentAttendance(sessionId, userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{sessionId}/attendance")
    public ResponseEntity<Map<String, Object>> getAttendanceSummary(@PathVariable String sessionId) {
        return ResponseEntity.ok(meetingService.getAttendanceSummary(sessionId));
    }

    @GetMapping("/{sessionId}/attendance/csv")
    public ResponseEntity<String> getAttendanceCsv(@PathVariable String sessionId) {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=attendance.csv")
                .body(meetingService.generateAttendanceCsv(sessionId));
    }
}
