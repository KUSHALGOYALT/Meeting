package com.example.learn.controller;

import com.example.learn.exception.AccessDeniedException;
import com.example.learn.exception.DatabaseException;
import com.example.learn.exception.NotFoundException;
import com.example.learn.model.StudyBuddyInteraction;
import com.example.learn.service.StudyBuddyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/study-buddy")
public class StudyBuddyController {
    @Autowired
    private StudyBuddyService studyBuddyService;

    @PostMapping("/{sessionId}/{studentId}")
    public ResponseEntity<?> askQuestion(@PathVariable String sessionId, @PathVariable String studentId,
                                         @RequestBody String question, Authentication auth) {
        try {
            checkStudent(auth);
            StudyBuddyInteraction interaction = studyBuddyService.askQuestion(studentId, sessionId, question);
            EntityModel<StudyBuddyInteraction> response = EntityModel.of(interaction,
                    linkTo(methodOn(StudyBuddyController.class).askQuestion(sessionId, studentId, question, auth)).withSelfRel(),
                    linkTo(methodOn(SessionController.class).getTeacherDashboard(sessionId)).withRel("dashboard"),
                    linkTo(methodOn(SessionController.class).generateWeeklyQuiz(sessionId, auth)).withRel("generate-weekly-quiz"),
                    linkTo(methodOn(SessionController.class).getSessions()).withRel("sessions")
            );
            return ResponseEntity.ok(response);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildErrorResponse(e));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildErrorResponse(e));
        } catch (DatabaseException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(buildErrorResponse(e));
        }
    }

    private void checkStudent(Authentication auth) {
        if (auth == null || !auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"))) {
            throw new AccessDeniedException("Access restricted to students");
        }
    }

    private EntityModel<Map<String, String>> buildErrorResponse(Exception e) {
        return EntityModel.of(Map.of("error", e.getMessage()),
                linkTo(methodOn(SessionController.class).getSessions()).withRel("sessions"));
    }
}