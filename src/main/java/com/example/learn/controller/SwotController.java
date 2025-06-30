package com.example.learn.controller;

import com.example.learn.model.EmotionData;
import com.example.learn.model.SwotAnalysis;
import com.example.learn.service.EmotionService;
import com.example.learn.service.SwotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/swot")
@CrossOrigin(origins = "http://localhost:3006")
public class SwotController {
    @Autowired
    private SwotService swotService;
    @Autowired
    private EmotionService emotionService;

    @PostMapping("/create")
    public SwotAnalysis createSwotAnalysis(@RequestBody SwotAnalysis swot) {
        return swotService.createSwotAnalysis(swot);
    }

    @GetMapping("/student/{studentId}")
    public List<SwotAnalysis> getStudentSwot(@PathVariable String studentId) {
        return swotService.getStudentSwot(studentId);
    }

    @PostMapping("/generate")
    public SwotAnalysis generateSwot(@RequestParam String studentId, @RequestParam String subject) {
        List<EmotionData> emotions = emotionService.getStudentEmotions(studentId);
        return swotService.generateSimpleSwot(studentId, subject, emotions);
    }
}
