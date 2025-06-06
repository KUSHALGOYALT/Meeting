package com.example.learn.controller;

import com.example.learn.model.Challenge;
import com.example.learn.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/challenge")
public class ChallengeController {
    @Autowired
    private ChallengeService challengeService;

    @PostMapping("/{sessionId}")
    public ResponseEntity<?> generateWeeklyChallenge(@PathVariable String sessionId, Authentication auth) {
        try {
            challengeService.generateWeeklyChallenge(sessionId);
            EntityModel<Map<String, String>> response = EntityModel.of(
                    Map.of("message", "Weekly challenge generated"),
                    linkTo(methodOn(ChallengeController.class).generateWeeklyChallenge(sessionId, auth)).withSelfRel(),
                    linkTo(methodOn(SessionController.class).getTeacherDashboard(sessionId)).withRel("dashboard")
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(EntityModel.of(Map.of("error", e.getMessage()),
                    linkTo(methodOn(SessionController.class).getSessions()).withRel("sessions")));
        }
    }
}