package com.example.learn.controller;

import com.example.learn.model.Meeting;
import com.example.learn.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meetings")
@CrossOrigin(origins = "http://localhost:3000")
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    @GetMapping("/all")
    public List<Meeting> getAllMeetings() {
        return meetingService.getAllMeetings();
    }

    @PostMapping("/create")
    public Meeting createMeeting(@RequestBody Meeting meeting) {
        return meetingService.createMeeting(meeting);
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Meeting> startMeeting(@PathVariable String id) {
        Meeting meeting = meetingService.startMeeting(id);
        return meeting != null ? ResponseEntity.ok(meeting) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/end")
    public ResponseEntity<Meeting> endMeeting(@PathVariable String id) {
        Meeting meeting = meetingService.endMeeting(id);
        return meeting != null ? ResponseEntity.ok(meeting) : ResponseEntity.notFound().build();
    }
}
