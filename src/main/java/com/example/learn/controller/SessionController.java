

package com.example.learn.controller;

import com.example.learn.model.Session;
import com.example.learn.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "http://localhost:3008")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @PostMapping("/manage")
    public Session manageSession(@RequestParam String sessionId, @RequestParam String studentId, @RequestParam String teacherId) {
        return sessionService.manageSession(sessionId, studentId, teacherId);
    }

    @GetMapping("/student/{studentId}")
    public List<Session> getStudentSessions(@PathVariable String studentId) {
        return sessionService.getStudentSessions(studentId);
    }

    @GetMapping("/teacher/{teacherId}")
    public List<Session> getTeacherSessions(@PathVariable String teacherId) {
        return sessionService.getTeacherSessions(teacherId);
    }

    @GetMapping("/quiz/{sessionId}/{studentId}")
    public List<String> generateSessionQuiz(@PathVariable String sessionId, @PathVariable String studentId) {
        return (List<String>) sessionService.generateSessionQuiz(sessionId, studentId);
    }
}
