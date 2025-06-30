package com.example.learn.controller;

import com.example.learn.model.EmotionData;
import com.example.learn.service.EmotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emotions")
@CrossOrigin(origins = "http://localhost:3003")
public class EmotionController {
    @Autowired
    private EmotionService emotionService;

    @PostMapping("/save")
    public EmotionData saveEmotionData(@RequestBody EmotionData emotionData) {
        return emotionService.saveEmotionData(emotionData);
    }

    @GetMapping("/student/{studentId}")
    public List<EmotionData> getStudentEmotions(@PathVariable String studentId) {
        return emotionService.getStudentEmotions(studentId);
    }

    @GetMapping("/meeting/{meetingId}")
    public List<EmotionData> getMeetingEmotions(@PathVariable String meetingId) {
        return emotionService.getMeetingEmotions(meetingId);
    }

    @GetMapping("/mood")
    public String getOverallMood(@RequestParam String studentId, @RequestParam String meetingId) {
        return emotionService.getOverallMood(studentId, meetingId);
    }
}
