package com.example.learn.controller;

import com.example.learn.service.EmotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/emotion")
public class EmotionController {
    @Autowired
    private EmotionService emotionService;

    @PostMapping("/{sessionId}/{studentId}")
    public ResponseEntity<?> updateEmotionFeedback(@PathVariable String sessionId, @PathVariable String studentId,
                                                   Authentication auth) {
        try {
            emotionService.updateEmotionFeedback(sessionId, studentId);
            EntityModel<Map<String, String>> response = EntityModel.of(
                    Map.of("message", "Emotion feedback updated"),
                    linkTo(methodOn(EmotionController.class).updateEmotionFeedback(sessionId, studentId, auth)).withSelfRel(),
                    linkTo(methodOn(SessionController.class).getTeacherDashboard(sessionId)).withRel("dashboard")
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(EntityModel.of(Map.of("error", e.getMessage()),
                    linkTo(methodOn(SessionController.class).getSessions()).withRel("sessions")));
        }
    }
}